package com.example.factory.model.api.friend;

import com.example.factory.model.FriendRequest;

import java.util.List;

/**
 * 查询接收到的好友请求
 */
public class SearchFriendRequestModel {

    //请求
    private String myId;

    //返回成功
    private String code;
    private String msg;
    private List<FriendRequest> data;

    //发送请求
    public SearchFriendRequestModel(String myId) {
        this.myId = myId;
    }

    //返回成功
    public SearchFriendRequestModel(String code, String msg, List<FriendRequest> data) {
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

    public List<FriendRequest> getData() {
        return data;
    }

    public void setData(List<FriendRequest> data) {
        this.data = data;
    }
}
