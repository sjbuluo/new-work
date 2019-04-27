package com.sun.health.newwork.base.dto;

public class ResponseDTO<T> {

    private int code;

    private String message;

    private String error;

    private T data;

    public ResponseDTO() {
    }

    public ResponseDTO(int code, String message, String error, T data) {
        this.code = code;
        this.message = message;
        this.error = error;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
