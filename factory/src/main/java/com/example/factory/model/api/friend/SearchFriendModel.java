package com.example.factory.model.api.friend;

import com.example.factory.model.User;

/**
 * 查询好友
 */
public class SearchFriendModel {

    //请求
    private String myId;
    private String friendUsername;

    //返回成功
    private String code;
    private String msg;
    private User data;

    //返回失败
    private String timestamp;
    private String status;
    private String error;
    private String message;
    private String path;

    //发送请求
    public SearchFriendModel(String myId, String friendUsername) {
        this.myId = myId;
        this.friendUsername = friendUsername;
    }

    //返回成功
    public SearchFriendModel(String code, String msg, User data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //返回失败
    public SearchFriendModel(String timestamp, String status, String error, String message, String path) {
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
