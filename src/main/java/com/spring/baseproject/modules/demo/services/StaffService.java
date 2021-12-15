package com.spring.baseproject.modules.demo.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.demo.models.dtos.StaffDto;
import com.spring.baseproject.modules.demo.models.dtos.UpdateStaffDto;
import com.spring.baseproject.modules.demo.models.entities.Staff;
import com.spring.baseproject.modules.demo.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> getStaffs() {
        //TODO business logic here
        return staffRepository.getStaffs();
    }

    public Staff findStaff(String username) throws ResponseException {
        //TODO business logic here
        Staff staff = staffRepository.getStaffByID(username);
        if (staff == null) {
            throw new ResponseException(ResponseValue.STAFF_NOT_FOUND);
        }
        return staff;
    }

    public void createNewStaff(StaffDto staffDto) throws ResponseException {
        //TODO business logic here
        Staff staff = staffRepository.getStaffByID(staffDto.getId());
        if (staff != null) {
            throw new ResponseException(ResponseValue.STAFF_ID_EXISTS);
        }
        Staff newStaff = new Staff(staffDto);
        staffRepository.saveStaff(newStaff);
    }

    public void updateStaff(String username, UpdateStaffDto updateStaffDto) throws ResponseException {
        //TODO business logic here
        Staff staff = staffRepository.getStaffByID(username);
        if (staff == null) {
            throw new ResponseException(ResponseValue.STAFF_NOT_FOUND);
        }
        staff.update(updateStaffDto);
        staffRepository.saveStaff(staff);
    }

    public void deleteStaff(String staffID) {
        //TODO business logic here
        staffRepository.deleteStaff(staffID);
    }

    public void deleteStaffs(Set<String> listStaffIDs) {
        //TODO business logic here
        staffRepository.deleteStaffHasIdIn(listStaffIDs);
    }
}
