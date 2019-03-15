package com.spring.baseproject.modules.rbac.models.entities;

import com.spring.baseproject.modules.rbac.models.dtos.FunctionDescriptionDto;

import javax.persistence.*;

@Entity
@Table(name = "api_function")
public class ApiFunction {
    public static final String DESCRIPTION = "description";
    public static final String API_MODULE = "apiModule";

    @Id
    @JoinColumn(name = "name")
    private String name;
    @JoinColumn(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "api_module")
    private ApiModule apiModule;

    public ApiFunction() {
    }

    public ApiFunction(String name) {
        setName(name);
        setApiModule(apiModule);
    }

    public ApiFunction(ApiModule apiModule, FunctionDescriptionDto functionDescription) {
        setName(functionDescription.getName());
        setDescription(functionDescription.getDescription());
        setApiModule(apiModule);
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

    public ApiModule getApiModule() {
        return apiModule;
    }

    public void setApiModule(ApiModule apiModule) {
        if (this.apiModule == null || !this.apiModule.equals(apiModule)) {
            this.apiModule = apiModule;
        }
    }
}
