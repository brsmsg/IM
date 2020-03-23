package com.example.factory.model.api.account;

import androidx.annotation.NonNull;

/**
 * @author brsmsg
 * @time 2020/3/9
 */
public class AccountModel {

    private String username;

    private String password;
    //成功返回状态码
    private String code;
    //成功返回消息
    private String msg;
    //成功返回数据
    private String data;
    //失败返回时间戳
    private String timeStamp;
    //失败返回状态码
    private String timestamp;
    //失败返回错误
    private String error;
    //失败返回message
    private String message;
    //失败返回url
    private String path;


    //发送
    public AccountModel(String username, String password) {
        this.username = username;
        this.password = password;
    }


    //返回成功
    public AccountModel(String code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //返回失败
    public AccountModel(String timeStamp, String timestamp, String error, String message, String path) {
        this.timeStamp = timeStamp;
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return getUsername() + "\n" + getPassword() + "\n"
                + getCode() + "\n" + getData() + "\n"
                +getMsg() + "\n";
    }
}
