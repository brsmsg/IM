package com.example.factory.model;

/**
 * @author brsmsg
 * @time 2020/3/25
 */
public class MsgUI {
    //接受消息
    public static final int TYPE_RECEIVED = 0;
    //发送消息
    public static final int TYPE_SEND = 1;
    //消息内容
    private String content;
    //头像
    private String portrait;
    //接受还是发送
    private int type;

    public MsgUI(String content, String portrait, int type) {
        this.content = content;
        this.portrait = portrait;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
