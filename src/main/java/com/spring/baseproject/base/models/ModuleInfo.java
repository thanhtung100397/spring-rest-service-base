package com.spring.baseproject.base.models;

import com.spring.baseproject.annotations.swagger.Module;

public class ModuleInfo {
    private String name;
    private Module module;

    public ModuleInfo(String name, Module module) {
        this.name = name;
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
