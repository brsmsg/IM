package com.example.factory.model.api.account.update;

/**
 * @author brsmsg
 * @time 2020/4/18
 */
public class UsernameModel {

    private String id;

    private String username;

//    private int code;

    private String msg;

//    private boolean data;

    public UsernameModel(String id, String username) {
        this.id = id;
        this.username = username;
    }

//    public UsernameModel(int code, String msg, boolean data) {
//        this.code = code;
//        this.msg = msg;
//        this.data = data;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

//    public boolean isData() {
//        return data;
//    }
//
//    public void setData(boolean data) {
//        this.data = data;
//    }
}
