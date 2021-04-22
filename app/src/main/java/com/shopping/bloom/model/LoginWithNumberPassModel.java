package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginWithNumberPassModel {

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginWithNumberPassModel(String mobile_no, String password) {
        this.mobile_no = mobile_no;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

}
