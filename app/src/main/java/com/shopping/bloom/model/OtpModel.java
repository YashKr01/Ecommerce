package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpModel {
    @SerializedName("mobile_no")
    @Expose
    String mobile_no;

    @SerializedName("otp")
    @Expose
    String otp;

    public OtpModel(String mobile_no, String otp) {
        this.mobile_no = mobile_no;
        this.otp = otp;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
