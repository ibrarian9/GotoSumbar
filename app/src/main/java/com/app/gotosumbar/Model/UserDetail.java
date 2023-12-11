package com.app.gotosumbar.Model;

public class UserDetail {

    private String userId, userName, userEmail, userNoHp;
    public UserDetail() {

    }
    public UserDetail(String userId, String userName, String userEmail, String userNoHp) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userNoHp = userNoHp;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNoHp() {
        return userNoHp;
    }

    public void setUserNoHp(String userNoHp) {
        this.userNoHp = userNoHp;
    }

}
