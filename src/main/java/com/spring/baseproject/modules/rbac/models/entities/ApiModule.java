package com.spring.baseproject.modules.rbac.models.entities;

import com.spring.baseproject.modules.rbac.models.dtos.ModuleDescriptionDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_module")
public class ApiModule {
    public static final String PRIORITY = "priority";

    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "priority")
    private int priority;

    public ApiModule() {
    }

    public ApiModule(String name, String description, int priority) {
        setName(name);
        setDescription(description);
        setPriority(priority);
    }

    public void update(ModuleDescriptionDto moduleDescriptionDto) {
        setDescription(moduleDescriptionDto.getDescription());
        setPriority(moduleDescriptionDto.getPriority() == null? 0 : moduleDescriptionDto.getPriority());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (this.description == null || !this.description.equals(description)) {
            this.description = description;
        }
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if(this.priority != priority) {
            this.priority = priority;
        }
    }
}
