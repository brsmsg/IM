package com.example.factory.model.api.Account;

/**
 * @author brsmsg
 * @time 2020/3/9
 */
public class LoginModel {

    private String userName;

    private String password;

    private String id;

    private String status;

    public LoginModel(String userName, String id, String status){
        this.userName = userName;
        this.id = id;
        this.status = status;
    }

    public LoginModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
