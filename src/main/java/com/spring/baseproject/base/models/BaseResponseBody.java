package com.spring.baseproject.base.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BaseResponseBody<T> {
    @ApiModelProperty(notes = "[http status code][special code]")
    private int code;
    @ApiModelProperty(notes = "responseBody message", position = 1)
    private String msg;
    @ApiModelProperty(notes = "responseBody data", position = 2)
    private T data;

    public BaseResponseBody() {
    }

    public BaseResponseBody(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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
