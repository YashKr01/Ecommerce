package com.shopping.bloom.model;

public class RegistrationModel {
    String name, email, mobile_no, firebase_token;

    public RegistrationModel(String name, String email, String mobile_no, String firebase_token) {
        this.name = name;
        this.email = email;
        this.mobile_no = mobile_no;
        this.firebase_token = firebase_token;
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
