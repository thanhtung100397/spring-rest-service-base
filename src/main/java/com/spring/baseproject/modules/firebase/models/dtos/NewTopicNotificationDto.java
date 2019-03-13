package com.spring.baseproject.modules.firebase.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel
public class NewTopicNotificationDto {
    @ApiModelProperty(notes = "target topic của notification")
    @NotEmpty
    private String topic;
    @ApiModelProperty(notes = "dữ liệu của notification", position = 1)
    private Object data;

    public NewTopicNotificationDto(@NotEmpty String topic, @NotNull Object data) {
        this.topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
