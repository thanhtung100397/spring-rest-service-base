package com.spring.baseproject.modules.demo.models.entities;

import com.spring.baseproject.modules.demo.models.dtos.StaffDto;
import com.spring.baseproject.modules.demo.models.dtos.UpdateStaffDto;

public class Staff {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public Staff() {
    }

    public Staff(StaffDto staffDto) {
        this.id = staffDto.getId();
        update(staffDto);
    }

    public void update(UpdateStaffDto updateStaffDto) {
        this.firstName = updateStaffDto.getFirstName();
        this.lastName = updateStaffDto.getLastName();
        this.email = updateStaffDto.getEmail();
        this.phone = updateStaffDto.getPhone();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
