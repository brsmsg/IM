package com.example.factory.model;

/**
 * @author brsmsg
 * @time 2020/4/19
 */
public class SearchUI {
    public static final int SEND = 0;
    public static final int NOT_SEND = 1;

    //id
    private String id;

    //用户名
    private String username;

    //头像
    private String faceImage;

    //个签
    private String description;

    //是否添加
    private int isSend;

    public SearchUI(String id, String username, String faceImage, String description) {
        this.id = id;
        this.username = username;
        this.faceImage = faceImage;
        this.description = description;
    }

    public SearchUI(String username, String faceImage, String description) {
        this.username = username;
        this.faceImage = faceImage;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }
}
