package com.spring.baseproject.base.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.constants.StringConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

@ApiModel
public class BaseResponseBody<T> {
    @ApiModelProperty(notes = "[http status code][special code]")
    @JsonProperty(StringConstants.CODE)
    private int code;
    @ApiModelProperty(notes = "thông điệp", position = 1)
    @JsonProperty(StringConstants.MESSAGE)
    private String msg;
    @ApiModelProperty(notes = "dữ liệu trả về", position = 2)
    @JsonProperty(StringConstants.DATA)
    private T data;

    public BaseResponseBody() {
    }

    public BaseResponseBody(ResponseValue responseValue, T data) {
        this.code = responseValue.specialCode();
        this.msg = responseValue.message();
        this.data = data;
    }

    public BaseResponseBody(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        if (data instanceof Page) {
            this.data = (T) new PageDto((Page) data);
        } else {
            this.data = data;
        }
    }

    public BaseResponseBody(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
