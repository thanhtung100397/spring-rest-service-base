package com.spring.baseproject.modules.firebase.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TopicNotificationResponse {
    @ApiModelProperty(notes = "mã định danh của notification đã được gửi đi")
    @JsonProperty("message_id")
    private String messageID;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}
