package com.spring.baseproject.modules.demo.services;

import com.spring.baseproject.BaseMockitoJUnitTests;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo.models.dtos.StaffDto;
import com.spring.baseproject.modules.demo.models.entities.Staff;
import com.spring.baseproject.modules.demo.repositories.StaffRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.instanceOf;

public class StaffServiceTests extends BaseMockitoJUnitTests {
    @InjectMocks
    private StaffService staffService;
    @Mock
    private StaffRepository staffRepository;

    private Map<String, Staff> staffs = new HashMap<>();

    @Override
    public void init() {
        //TODO define all mocks for all method of StaffRepository which were used in StaffService

        when(staffRepository.getStaffs())
                .then((Answer<List<Staff>>) invocation -> {
                    List<Staff> result = new ArrayList<>(staffs.values());
                    result.sort(Comparator.comparing(Staff::getId));
                    return result;
                });

        when(staffRepository.getStaffByID(anyString()))
                .then((Answer<Staff>) invocation -> {
                    String staffID = invocation.getArgument(0);
                    return staffs.get(staffID);
                });

        doAnswer(invocation -> {
            Staff staff = invocation.getArgument(0);
            staffs.put(staff.getId(), staff);
            return null;
        }).when(staffRepository).saveStaff(any(Staff.class));

        doAnswer(invocation -> {
            String staffID = invocation.getArgument(0);
            staffs.remove(staffID);
            return null;
        }).when(staffRepository).deleteStaff(anyString());
    }

    @Override
    public void clear() {
        staffs.clear();
    }

    @Test
    public void testGetStaffs_SUCCESS() {
        Staff staff1 = new Staff();
        staff1.setId("12345");
        staff1.setFirstName("Foo");
        staff1.setLastName("Bar");
        staff1.setEmail("email@abc.xyz");
        staff1.setPhone("09123456789");

        Staff staff2 = new Staff();
        staff2.setId("1234");
        staff2.setFirstName("Baz");
        staff2.setLastName("Bar");
        staff2.setEmail("abc@def.ghi");
        staff2.setPhone("0987654321");

        staffs.put(staff1.getId(), staff1);
        staffs.put(staff2.getId(), staff2);

        BaseResponse response = staffService.getStaffs();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));
        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        List<Staff> staff = new ArrayList<>();
        try {
            staff = (List<Staff>) responseBody.getData();
        } catch (Exception ignore) {
        }
        assertEquals(2, staff.size());
        assertEquals(staff2, staff.get(0));
        assertEquals(staff1, staff.get(1));
    }

    @Test
    public void testFindStaff_SUCCESS() {
        Staff staff = new Staff();
        staff.setId("12345");
        staff.setFirstName("Foo");
        staff.setLastName("Bar");
        staff.setEmail("email@abc.xyz");
        staff.setPhone("0912345678");
        staffs.put(staff.getId(), staff);

        BaseResponse response = staffService.findStaff(staff.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        assertThat(responseBody.getData(), instanceOf(Staff.class));
        Staff responseStaff = (Staff) responseBody.getData();
        assertEquals(staff.getId(), responseStaff.getId());
        assertEquals(staff.getFirstName(), responseStaff.getFirstName());
        assertEquals(staff.getLastName(), responseStaff.getLastName());
        assertEquals(staff.getEmail(), responseStaff.getEmail());
        assertEquals(staff.getPhone(), responseStaff.getPhone());
    }

    @Test
    public void testFindStaff_STAFF_NOT_FOUND() {
        BaseResponse response = staffService.findStaff("12345");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(ResponseValue.STAFF_NOT_FOUND.specialCode(), responseBody.getCode());
        assertEquals(0, staffs.size());
    }

    @Test
    public void testCreateNewStaff_SUCCESS() {
        StaffDto staffDto = new StaffDto();
        staffDto.setId("12345");
        staffDto.setFirstName("Foo");
        staffDto.setLastName("Bar");
        staffDto.setEmail("email@abc.xyz");
        staffDto.setPhone("0912345678");

        BaseResponse response = staffService.createNewStaff(staffDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        Staff staff = staffs.get(staffDto.getId());
        assertNotNull(staff);
        assertEquals(staffDto.getId(), staff.getId());
        assertEquals(staffDto.getFirstName(), staff.getFirstName());
        assertEquals(staffDto.getLastName(), staff.getLastName());
        assertEquals(staffDto.getEmail(), staff.getEmail());
        assertEquals(staffDto.getPhone(), staff.getPhone());
    }

    @Test
    public void testCreateNewStaff_STAFF_ID_EXITS() {
        Staff staff = new Staff();
        staff.setId("12345");
        staff.setFirstName("Foo");
        staff.setLastName("Bar");
        staff.setEmail("email@abc.xyz");
        staff.setPhone("0912345678");
        staffs.put(staff.getId(), staff);

        StaffDto staffDto = new StaffDto();
        staffDto.setId("12345");
        staffDto.setFirstName("Foo");
        staffDto.setLastName("Bar");
        staffDto.setEmail("abc@def.ghi");
        staffDto.setPhone("0987654321");

        BaseResponse response = staffService.createNewStaff(staffDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(ResponseValue.STAFF_ID_EXISTS.specialCode(), responseBody.getCode());

        Staff staffExist = staffs.get(staffDto.getId());
        assertNotNull(staffExist);
        assertEquals(staff.getId(), staffExist.getId());
        assertEquals(staff.getFirstName(), staffExist.getFirstName());
        assertEquals(staff.getLastName(), staffExist.getLastName());
        assertEquals(staff.getEmail(), staffExist.getEmail());
        assertEquals(staff.getPhone(), staffExist.getPhone());
    }

    @Test
    public void testUpdateStaff_SUCCESS() {
        Staff staff = new Staff();
        staff.setId("12345");
        staff.setFirstName("Foo");
        staff.setLastName("Bar");
        staff.setEmail("email@abc.xyz");
        staff.setPhone("0912345678");
        staffs.put(staff.getId(), staff);

        StaffDto staffDto = new StaffDto();
        staff.setFirstName("Baz");
        staff.setLastName("Foo");
        staffDto.setEmail("abc@def.ghi");
        staffDto.setPhone("0987654321");

        BaseResponse response = staffService.updateStaff(staff.getId(), staffDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        Staff staffExist = staffs.get(staff.getId());
        assertEquals(1, staffs.size());
        assertEquals(staffDto.getFirstName(), staffExist.getFirstName());
        assertEquals(staffDto.getLastName(), staffExist.getLastName());
        assertEquals(staffDto.getEmail(), staffExist.getEmail());
        assertEquals(staffDto.getPhone(), staffExist.getPhone());
    }

    @Test
    public void testUpdateStaff_STAFF_NOT_FOUND() {
        StaffDto staffDto = new StaffDto();
        staffDto.setId("12345");
        staffDto.setFirstName("Foo");
        staffDto.setLastName("Bar");
        staffDto.setEmail("abc@def.ghi");
        staffDto.setPhone("0987654321");

        BaseResponse response = staffService.updateStaff("12345", staffDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(ResponseValue.STAFF_NOT_FOUND.specialCode(), responseBody.getCode());

        assertEquals(0, staffs.size());
    }

    @Test
    public void testDeleteStaff_SUCCESS() {
        Staff staff = new Staff();
        staff.setId("12345");
        staff.setFirstName("Foo");
        staff.setLastName("Bar");
        staff.setEmail("email@abc.xyz");
        staff.setPhone("0912345678");
        staffs.put(staff.getId(), staff);

        BaseResponse response = staffService.deleteStaff("12345");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), instanceOf(BaseResponseBody.class));

        BaseResponseBody responseBody = (BaseResponseBody) response.getBody();
        assertEquals(HttpStatus.OK.value(), responseBody.getCode());

        assertEquals(0, staffs.size());
    }
}
