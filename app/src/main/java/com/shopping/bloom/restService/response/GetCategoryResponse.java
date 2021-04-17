package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.models.Product;

import java.util.List;

public class GetCategoryResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("date")
    private List<Product> data;

    @SerializedName("message")
    private String message;

    public GetCategoryResponse(boolean success, List<Product> data, String message) {
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

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
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
        return "GetCategoryResponse{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
