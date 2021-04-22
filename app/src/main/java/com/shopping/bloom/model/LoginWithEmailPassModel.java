package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginWithEmailPassModel {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginWithEmailPassModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_no() {
        return email;
    }

    public void setMobile_no(String email) {
        this.email = email;
    }

}
