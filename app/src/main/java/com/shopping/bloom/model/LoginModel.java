package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginModel{

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    public LoginModel(){};

    public LoginModel(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
