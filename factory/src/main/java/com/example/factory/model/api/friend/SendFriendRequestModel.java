package com.example.factory.model.api.friend;

public class SendFriendRequestModel {
    //发送请求
    private String myId;
    private String friendUsername;

    //返回成功
    private String code;
    private String msg;
    private String data;

    //失败返回
    private String timestamp;

    private String status;

    private String error;

    private String message;

    private String path;

    //发送请求
    public SendFriendRequestModel(String myId, String friendUsername) {
        this.myId = myId;
        this.friendUsername = friendUsername;
    }

    //返回成功
    public SendFriendRequestModel(String code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public SendFriendRequestModel(String timestamp, String status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
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
