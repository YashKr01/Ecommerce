package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginWithPassData {

    @SerializedName("userData")
    @Expose
    UserInfo userInfo;

    @SerializedName("token")
    @Expose
    String token;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
