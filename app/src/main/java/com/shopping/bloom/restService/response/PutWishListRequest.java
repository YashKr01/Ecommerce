package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PutWishListRequest {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<String> data;

    @SerializedName("message")
    private String message;

    public PutWishListRequest(boolean success, List<String> data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PutWishListRequest{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
