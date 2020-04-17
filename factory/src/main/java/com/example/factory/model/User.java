package com.example.factory.model;

/**
 * @author brsmsg
 * @time 2020/3/13
 */
public class User {
    //id
    private String id;

    //用户名
    private String username;

    //头像
    private String faceImage;

    //描述
    private String description;

    public User(String id, String username, String faceImage, String description) {
        this.id = id;
        this.username = username;
        this.faceImage = faceImage;
        this.description = description;
    }

    public User(String username, String faceImage, String description) {
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
    public User(String id) {
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


}


