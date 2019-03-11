package com.spring.baseproject.modules.demo.models.dtos;

import com.spring.baseproject.annotations.validator.text.no_space.NoSpace;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StaffDto extends UpdateStaffDto {
    @ApiModelProperty(notes = "mã định danh", example = "NOT_EMPTY, ANY_CHARACTER, NO_SPACE")
    @NoSpace
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
