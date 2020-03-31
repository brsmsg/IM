package com.example.factory.model.api.contact;

import com.example.factory.model.User;
import com.example.factory.model.db.Contact;

import java.util.List;

/**
 * @author brsmsg
 * @time 2020/3/23
 */
public class GetContact {
    private String code;

    private String msg;

    private List<Contact> data;

    public GetContact(String code, String msg, List<Contact> data) {
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

    public List<Contact> getData() {
        return data;
    }

    public void setData(List<Contact> data) {
        this.data = data;
    }
}
