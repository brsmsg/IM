package com.example.factory.model.api;

/**
 * @author brsmsg
 * @time 2020/4/18
 */
public class ResponseModel {
    private int code;

    private String msg;

    private boolean data;

    public ResponseModel(int code, String msg, boolean data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseModel(int code, String msg) {
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

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
