package com.spring.baseproject.modules.demo.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo.models.dtos.StaffDto;
import com.spring.baseproject.modules.demo.models.dtos.UpdateStaffDto;
import com.spring.baseproject.modules.demo.models.entities.Staff;
import com.spring.baseproject.modules.demo.services.StaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/demo")
@Api(description = "Quản lý nhân viên")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @ApiOperation(value = "Lấy danh sách nhân viên",
            notes = "Lấy danh sách toàn bộ nhân viên có trong database, không hỗ trợ sắp xếp và phân trang",
            response = Iterable.class)
    @Responses(value = {
    })
    @GetMapping("/staffs")
    public List<Staff> getStaffs() {
        return staffService.getStaffs();
    }

    @ApiOperation(value = "Lấy thông tin nhân viên bằng id",
            notes = "Lấy thông tin một nhân viên dựa trên id của nhân viên được truyển lên",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.STAFF_NOT_FOUND)
    })
    @GetMapping("/staffs/{sid}")
    public Staff getStaff(@PathVariable("sid") String staffID) throws ResponseException {
        return staffService.findStaff(staffID);
    }

    @ApiOperation(value = "Tạo mới nhân viên",
            notes = "Tạo mới một nhân viên, có kiểm tra id tồn tại, " +
                    "có validate các tham số trong request body",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.STAFF_ID_EXISTS)
    })
    @PostMapping("/staffs")
    public void createNewStaff(@RequestBody @Valid StaffDto staffDto) throws ResponseException {
        staffService.createNewStaff(staffDto);
    }

    @ApiOperation(value = "Cập nhật nhân viên",
            notes = "Cập nhật thông tin một nhân viên đã tồn tại, " +
                    "có validate các tham số trong request body",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class),
            @Response(responseValue = ResponseValue.STAFF_NOT_FOUND)
    })
    @PutMapping("/staffs/{sid}")
    public void updateStaff(@PathVariable("sid") String staffID,
                            @RequestBody @Valid UpdateStaffDto updateStaffDto) throws ResponseException {
        staffService.updateStaff(staffID, updateStaffDto);
    }

    @ApiOperation(value = "Xóa nhân viên theo id",
            notes = "Xóa nhân viên dựa trên id của nhân viên được truyển lên",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/staffs/{sid}")
    public void deleteStaff(@PathVariable("sid") String staffID) {
        staffService.deleteStaff(staffID);
    }

    @ApiOperation(value = "Xóa danh sách nhân viên",
            notes = "Xóa nhiều nhân viên dựa trên một danh sách các id nhân viên được truyển lên, " +
                    "cho phép duplicate id",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/staffs")
    public void deleteStaffs(@RequestBody Set<String> listStaffIDs) {
        staffService.deleteStaffs(listStaffIDs);
    }
}
