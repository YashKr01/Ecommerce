package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationModel {

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("mobile_no")
    @Expose
    String mobile_no;

    @SerializedName("password")
    @Expose
    String password;

    @SerializedName("firebase_token")
    @Expose
    String firebase_token;

    @SerializedName("imei_number")
    @Expose
    String imei_number;

    @SerializedName("device_type")
    @Expose
    String device_type;

    public RegistrationModel(String name, String email, String mobile_no, String password, String firebase_token, String imei_number, String device_type) {
        this.name = name;
        this.email = email;
        this.mobile_no = mobile_no;
        this.password = password;
        this.firebase_token = firebase_token;
        this.imei_number = imei_number;
        this.device_type = device_type;
    }

    public String getImei_number() {
        return imei_number;
    }

    public void setImei_number(String imei_number) {
        this.imei_number = imei_number;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(String firebase_token) {
        this.firebase_token = firebase_token;
    }
}
