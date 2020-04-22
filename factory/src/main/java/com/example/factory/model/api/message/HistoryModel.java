package com.example.factory.model.api.message;

import com.example.factory.model.api.History;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/4/21
 */
public class HistoryModel {
    private int code;

    private String msg;

    private List<History> data;

    public HistoryModel(int code, String msg, List<History> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public List<History> getData() {
        return data;
    }

    public void setData(List<History> data) {
        this.data = data;
    }
}
