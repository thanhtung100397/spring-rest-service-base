package com.spring.baseproject.modules.rbac.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_method")
public class ApiMethod {
    public static final String PRIORITY = "priority";

    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "priority")
    private int priority;

    public ApiMethod() {
    }

    public ApiMethod(String name, Integer priority) {
        this.name = name;
        this.priority = priority == null? 0 : priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
