package com.example.factory.model.api.Account;

/**
 * @author brsmsg
 * @time 2020/3/11
 */
public class RegisterModel {

    String userName;

    String password;

    String status;

    public RegisterModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public RegisterModel(String status){
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
