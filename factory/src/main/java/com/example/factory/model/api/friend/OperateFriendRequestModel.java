package com.example.factory.model.api.friend;

public class OperateFriendRequestModel {

    //发送请求
    private String myId;
    private String friendId;
    private int operateType;

    //返回
    private String code;
    private String msg;
    private String data;

    //发送请求
    public OperateFriendRequestModel(String myId, String friendId, int operateType) {
        this.myId = myId;
        this.friendId = friendId;
        this.operateType = operateType;
    }

    //返回
    public OperateFriendRequestModel(String code, String msg, String data) {
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

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
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
