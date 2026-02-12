package com.CardMaster.dto;

public class ResponseStructure<T> {
    private String msg;
    private T data;

    // Getters and Setters
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
