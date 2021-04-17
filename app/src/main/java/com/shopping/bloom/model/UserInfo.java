package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("mobile_no")
    @Expose
    String mobile_no;

    @SerializedName("firebase_token")
    @Expose
    String firebase_token;

    @SerializedName("email_verified_at")
    @Expose
    String email_verified_at;

    @SerializedName("primary_address")
    @Expose
    String primary_address;

    @SerializedName("primary_pincode")
    @Expose
    String primary_pincode;

    @SerializedName("city")
    @Expose
    String city;

    @SerializedName("amount_spent")
    @Expose
    String amount_spent;

    @SerializedName("last_ordered_at")
    @Expose
    String last_ordered_at;

    @SerializedName("wallet_key")
    @Expose
    String wallet_key;

    @SerializedName("mobile_otp")
    @Expose
    String mobile_otp;

    @SerializedName("email_otp")
    @Expose
    String email_otp;

    @SerializedName("mobile_otp_time")
    @Expose
    String mobile_otp_time;

    @SerializedName("email_otp_time")
    @Expose
    String email_otp_time;

    @SerializedName("deleted_at")
    @Expose
    String deleted_at;

    @SerializedName("created_at")
    @Expose
    String created_at;

    @SerializedName("updated_at")
    @Expose
    String updated_at;

    @SerializedName("imei_number")
    @Expose
    String imei_number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getPrimary_address() {
        return primary_address;
    }

    public void setPrimary_address(String primary_address) {
        this.primary_address = primary_address;
    }

    public String getPrimary_pincode() {
        return primary_pincode;
    }

    public void setPrimary_pincode(String primary_pincode) {
        this.primary_pincode = primary_pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAmount_spent() {
        return amount_spent;
    }

    public void setAmount_spent(String amount_spent) {
        this.amount_spent = amount_spent;
    }

    public String getLast_ordered_at() {
        return last_ordered_at;
    }

    public void setLast_ordered_at(String last_ordered_at) {
        this.last_ordered_at = last_ordered_at;
    }

    public String getWallet_key() {
        return wallet_key;
    }

    public void setWallet_key(String wallet_key) {
        this.wallet_key = wallet_key;
    }

    public String getMobile_otp() {
        return mobile_otp;
    }

    public void setMobile_otp(String mobile_otp) {
        this.mobile_otp = mobile_otp;
    }

    public String getEmail_otp() {
        return email_otp;
    }

    public void setEmail_otp(String email_otp) {
        this.email_otp = email_otp;
    }

    public String getMobile_otp_time() {
        return mobile_otp_time;
    }

    public void setMobile_otp_time(String mobile_otp_time) {
        this.mobile_otp_time = mobile_otp_time;
    }

    public String getEmail_otp_time() {
        return email_otp_time;
    }

    public void setEmail_otp_time(String email_otp_time) {
        this.email_otp_time = email_otp_time;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getImei_number() {
        return imei_number;
    }

    public void setImei_number(String imei_number) {
        this.imei_number = imei_number;
    }
}
