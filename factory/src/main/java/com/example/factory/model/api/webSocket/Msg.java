package com.example.factory.model.api.webSocket;

/**
 * @author brsmsg
 * @time 2020/3/31
 */
public class Msg {
    //发送人id
    private String sendUserId;
    //收信人id
    private String receiveUserId;
    //消息内容
    private String msg;
    //消息id
    private String msgId;

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

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
