package com.spring.baseproject.modules.demo.repositories;

import com.spring.baseproject.modules.demo.models.entities.Staff;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StaffRepository {
    private Map<String, Staff> staffs = new HashMap<>();

    public List<Staff> getStaffs() {
        // TODO datasource interaction here
        List<Staff> result = new ArrayList<>(staffs.values());
        result.sort(Comparator.comparing(Staff::getId));
        return result;
    }

    public Staff getStaffByID(String id) {
        // TODO datasource interaction here
        return staffs.get(id);
    }

    public void saveStaff(Staff staff) {
        // TODO datasource interaction here
        staffs.put(staff.getId(), staff);
    }

    public void deleteStaff(String staffID) {
        // TODO datasource interaction here
        staffs.remove(staffID);
    }

    public void deleteStaffHasIdIn(Set<String> listStaffIDs) {
        // TODO datasource interaction here
        for (String staffID : listStaffIDs) {
            staffs.remove(staffID);
        }
    }
}
