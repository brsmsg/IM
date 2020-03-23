package com.example.factory.model.api.contact;

import com.example.factory.model.User;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/23
 */
public class Contact {
    private String code;

    private String msg;

    private List<User> data;

    public Contact(String code, String msg, List<User> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
