package com.example.factory.model.api;

/**
 * @author brsmsg
 * @time 2020/4/21
 */
public class History {
    private String sendUserId;

    private String receiveUserId;

    private String msg;

    private String createTime;

    public History(String sendUserId, String receiveUserId, String msg, String createTime) {
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.msg = msg;
        this.createTime = createTime;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
