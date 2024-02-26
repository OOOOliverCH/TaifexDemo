package com.taifexdemo.entity;

//信息反饋實體類
public class ReturnMessage {

    private String code;
    private String message;

    public ReturnMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
