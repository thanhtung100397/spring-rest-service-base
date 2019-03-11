package com.spring.baseproject.modules.demo.controllers;

import com.spring.baseproject.annotations.swagger.Response;
import com.spring.baseproject.annotations.swagger.Responses;
import com.spring.baseproject.base.controllers.BaseRESTController;
import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.BaseResponseBody;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo.models.dtos.StaffDto;
import com.spring.baseproject.modules.demo.models.dtos.UpdateStaffDto;
import com.spring.baseproject.modules.demo.services.StaffService;
import com.spring.baseproject.swagger.demo.demo_controller.ListStaffDtosSwagger;
import com.spring.baseproject.swagger.demo.demo_controller.StaffDtoSwagger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/demo")
@Api(description = "Quản lý nhân viên")
public class StaffController extends BaseRESTController {
    @Autowired
    private StaffService staffService;

    @ApiOperation(value = "Lấy danh sách nhân viên",
            notes = "Lấy danh sách toàn bộ nhân viên có trong database, không hỗ trợ sắp xếp và phân trang",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = ListStaffDtosSwagger.class)
    })
    @GetMapping("/staffs")
    public BaseResponse getStaffs() {
        return staffService.getStaffs();
    }

    @ApiOperation(value = "Lấy thông tin nhân viên bằng id",
            notes = "Lấy thông tin một nhân viên dựa trên id của nhân viên được truyển lên",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = StaffDtoSwagger.class),
            @Response(responseValue = ResponseValue.STAFF_NOT_FOUND)
    })
    @GetMapping("/staffs/{sid}")
    public BaseResponse getStaff(@PathVariable("sid") String staffID) {
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
    public BaseResponse createNewStaff(@RequestBody @Valid StaffDto staffDto) {
        return staffService.createNewStaff(staffDto);
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
    public BaseResponse updateStaff(@PathVariable("sid") String staffID,
                                   @RequestBody @Valid UpdateStaffDto updateStaffDto) {
        return staffService.updateStaff(staffID, updateStaffDto);
    }

    @ApiOperation(value = "Xóa nhân viên theo id",
            notes = "Xóa nhân viên dựa trên id của nhân viên được truyển lên",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/staffs/{sid}")
    public BaseResponse deleteStaff(@PathVariable("sid") String staffID) {
        return staffService.deleteStaff(staffID);
    }

    @ApiOperation(value = "Xóa danh sách nhân viên",
            notes = "Xóa nhiều nhân viên dựa trên một danh sách các id nhân viên được truyển lên, " +
                    "cho phép duplicate id",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = BaseResponseBody.class)
    })
    @DeleteMapping("/staffs")
    public BaseResponse deleteStaffs(@RequestBody Set<String> listStaffIDs) {
        return staffService.deleteStaffs(listStaffIDs);
    }
}
