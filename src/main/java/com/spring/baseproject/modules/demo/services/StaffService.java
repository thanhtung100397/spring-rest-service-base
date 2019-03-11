package com.spring.baseproject.modules.demo.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.demo.models.dtos.StaffDto;
import com.spring.baseproject.modules.demo.models.dtos.UpdateStaffDto;
import com.spring.baseproject.modules.demo.models.entities.Staff;
import com.spring.baseproject.modules.demo.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public BaseResponse getStaffs() {
        //TODO business logic here
        return new BaseResponse<>(ResponseValue.SUCCESS, staffRepository.getStaffs());
    }

    public BaseResponse findStaff(String username) {
        //TODO business logic here
        Staff staff = staffRepository.getStaffByID(username);
        if (staff == null) {
            return new BaseResponse(ResponseValue.STAFF_NOT_FOUND);
        }
        return new BaseResponse<>(ResponseValue.SUCCESS, staff);
    }

    public BaseResponse createNewStaff(StaffDto staffDto) {
        //TODO business logic here
        Staff staff = staffRepository.getStaffByID(staffDto.getId());
        if (staff != null) {
            return new BaseResponse(ResponseValue.STAFF_ID_EXISTS);
        }
        Staff newStaff = new Staff(staffDto);
        staffRepository.saveStaff(newStaff);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse updateStaff(String username, UpdateStaffDto updateStaffDto) {
        //TODO business logic here
        Staff staff = staffRepository.getStaffByID(username);
        if (staff == null) {
            return new BaseResponse(ResponseValue.STAFF_NOT_FOUND);
        }
        staff.update(updateStaffDto);
        staffRepository.saveStaff(staff);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse deleteStaff(String staffID) {
        //TODO business logic here
        staffRepository.deleteStaff(staffID);
        return new BaseResponse(ResponseValue.SUCCESS);
    }

    public BaseResponse deleteStaffs(Set<String> listStaffIDs) {
        //TODO business logic here
        staffRepository.deleteStaffHasIdIn(listStaffIDs);
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
