package com.spring.baseproject.modules.demo.services;

import com.spring.baseproject.BaseMockitoJUnitTests;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
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
import static com.spring.baseproject.utils.AssertThrowable.*;

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

        List<Staff> listStaffs = staffService.getStaffs();
        assertEquals(2, listStaffs.size());
        assertEquals(staff2, listStaffs.get(0));
        assertEquals(staff1, listStaffs.get(1));
    }

    @Test
    public void testFindStaff_SUCCESS() throws ResponseException {
        Staff staff = new Staff();
        staff.setId("12345");
        staff.setFirstName("Foo");
        staff.setLastName("Bar");
        staff.setEmail("email@abc.xyz");
        staff.setPhone("0912345678");
        staffs.put(staff.getId(), staff);

        Staff foundStaff = staffService.findStaff(staff.getId());
        assertEquals(staff.getId(), foundStaff.getId());
        assertEquals(staff.getFirstName(), foundStaff.getFirstName());
        assertEquals(staff.getLastName(), foundStaff.getLastName());
        assertEquals(staff.getEmail(), foundStaff.getEmail());
        assertEquals(staff.getPhone(), foundStaff.getPhone());
    }

    @Test
    public void testFindStaff_STAFF_NOT_FOUND() throws ResponseException {
        assertThrows(
                () -> staffService.findStaff("12345"),
                (throwable) -> {
                    assertThat(throwable, instanceOf(ResponseException.class));
                    ResponseException responseException = (ResponseException) throwable;
                    assertEquals(ResponseValue.STAFF_NOT_FOUND, responseException.getResponseValue());
                });
    }

    @Test
    public void testCreateNewStaff_SUCCESS() throws ResponseException {
        StaffDto staffDto = new StaffDto();
        staffDto.setId("12345");
        staffDto.setFirstName("Foo");
        staffDto.setLastName("Bar");
        staffDto.setEmail("email@abc.xyz");
        staffDto.setPhone("0912345678");

        staffService.createNewStaff(staffDto);

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

        assertThrows(
                () -> staffService.createNewStaff(staffDto),
                (throwable) -> {
                    assertThat(throwable, instanceOf(ResponseException.class));
                    ResponseException responseException = (ResponseException) throwable;
                    assertEquals(ResponseValue.STAFF_ID_EXISTS, responseException.getResponseValue());
                });

        Staff staffExist = staffs.get(staffDto.getId());
        assertNotNull(staffExist);
        assertEquals(staff.getId(), staffExist.getId());
        assertEquals(staff.getFirstName(), staffExist.getFirstName());
        assertEquals(staff.getLastName(), staffExist.getLastName());
        assertEquals(staff.getEmail(), staffExist.getEmail());
        assertEquals(staff.getPhone(), staffExist.getPhone());
    }

    @Test
    public void testUpdateStaff_SUCCESS() throws ResponseException {
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

        staffService.updateStaff(staff.getId(), staffDto);

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

        assertThrows(
                () -> staffService.updateStaff("12345", staffDto),
                (throwable) -> {
                    assertThat(throwable, instanceOf(ResponseException.class));
                    ResponseException responseException = (ResponseException) throwable;
                    assertEquals(ResponseValue.STAFF_NOT_FOUND, responseException.getResponseValue());
                });

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

        staffService.deleteStaff("12345");

        assertEquals(0, staffs.size());
    }
}
