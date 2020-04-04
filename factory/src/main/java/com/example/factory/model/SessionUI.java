package com.example.factory.model;

import androidx.annotation.Nullable;

/**
 * @author brsmsg
 * @time 2020/4/4
 */
public class SessionUI {
    private String id;

    private String portrait;

    private String username;

    private String lastMsg;

    public SessionUI(String id, String portrait, String username, String lastMsg) {
        this.id = id;
        this.portrait = portrait;
        this.username = username;
        this.lastMsg = lastMsg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

}
