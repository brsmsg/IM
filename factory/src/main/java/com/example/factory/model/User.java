package com.example.factory.model;

/**
 * @author brsmsg
 * @time 2020/3/13
 */
public class User {

    private String id;

    private String userName;

    private String portrait;

    private String desc;

    private String gender;

    public User(String id){
        this.id = id;
    }

    public User(String userName, String portrait, String desc) {
        this.userName = userName;
        this.portrait = portrait;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
