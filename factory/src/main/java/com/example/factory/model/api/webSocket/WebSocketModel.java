package com.example.factory.model.api.webSocket;

/**
 * @author brsmsg
 * @time 2020/3/31
 */
public class WebSocketModel {
    //请求动作类型
    //1：连接， 2：聊天， 3：签收消息， 4：keep_alive
    private int action;
    //消息
    private Msg message;
    //签收id
    private String extend;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Msg getMessage() {
        return message;
    }

    public void setMessage(Msg message) {
        this.message = message;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
