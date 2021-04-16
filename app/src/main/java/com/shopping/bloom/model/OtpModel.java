package com.shopping.bloom.model;

public class OtpModel {
    String mobile_no, otp;

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
