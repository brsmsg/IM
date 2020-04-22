package com.example.factory.model;

/**
 * 用于查询接收到的好友请求
 */
public class FriendRequest {
    //自己的id
    private String id;
    //发送者的id
    private String sendUserId;
    //接收者的id
    private String receiveUserId;
    //是否接受
    private int isAccept;
    //发送请求的时间
    private String requestDateTime;
    //用户名
    private String username;


    public FriendRequest(String id, String sendUserId, String receiveUserId, int isAccept, String requestDateTime, String username) {
        this.id = id;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.isAccept = isAccept;
        this.requestDateTime = requestDateTime;
        this.username = username;
    }

    public FriendRequest(String username, String requestDateTime) {
        this.username = username;
        this.requestDateTime = requestDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public int getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(int isAccept) {
        this.isAccept = isAccept;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
