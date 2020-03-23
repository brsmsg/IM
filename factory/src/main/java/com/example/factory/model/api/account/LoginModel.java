package com.example.factory.model.api.account;

import com.example.factory.model.User;

/**
 * @author brsmsg
 * @time 2020/3/23
 */
public class LoginModel{
    //发送
    private String username;

    private String password;

    // 成功返回
    private String code;

    private String msg;

    private User data;

    //失败返回
    private String timestamp;

    private String status;

    private String error;

    private String message;

    private String path;

    //发送请求
    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //成功
    public LoginModel(String code, String msg, User data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //失败
    public LoginModel(String timestamp, String status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
