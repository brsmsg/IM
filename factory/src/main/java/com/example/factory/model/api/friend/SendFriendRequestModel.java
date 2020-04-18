package com.example.factory.model.api.friend;

public class SendFriendRequestModel {
    //发送请求
    private String myId;
    private String friendUsername;

    //返回成功
    private String code;
    private String msg;
    private String data;

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
}
