package com.example.factory.model.api.account;

/**
 * The type Personal model.
 * 个人信息界面
 */
public class PersonalModel {

    private String userName;

    private String portrait;

    private String password;

    private String id;

    public PersonalModel(String userName, String portrait, String password, String id){
        this.userName=userName;
        this.portrait=portrait;
        this.password=password;
        this.id=id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
