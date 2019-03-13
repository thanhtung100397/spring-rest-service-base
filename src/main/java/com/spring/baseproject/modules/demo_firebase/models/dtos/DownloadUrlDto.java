package com.spring.baseproject.modules.demo_firebase.models.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DownloadUrlDto {
    @ApiModelProperty(notes = "url tải xuống tệp tin")
    private String downloadUrl;

    public DownloadUrlDto() {
    }

    public DownloadUrlDto(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
