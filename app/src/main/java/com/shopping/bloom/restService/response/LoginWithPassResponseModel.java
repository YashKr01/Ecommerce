package com.shopping.bloom.restService.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.Data;
import com.shopping.bloom.model.LoginWithPassData;

public class LoginWithPassResponseModel {
    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("data")
    @Expose
    LoginWithPassData data;

    @SerializedName("message")
    @Expose
    String message;

    public LoginWithPassData getData() {
        return data;
    }

    public void setData(LoginWithPassData data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
