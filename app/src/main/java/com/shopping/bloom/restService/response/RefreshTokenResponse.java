package com.shopping.bloom.restService.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.SplashData;

public class RefreshTokenResponse {

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("success")
    @Expose
    boolean success;

    @SerializedName("Body")
    @Expose
    SplashData data;

    public SplashData getData() {
        return data;
    }

    public void setData(SplashData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
