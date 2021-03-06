package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.Product;

import java.util.List;

public class GetProductsResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<Product> data;

    @SerializedName("message")
    private String message;

    public GetProductsResponse(boolean success, List<Product> data, String message) {
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
        return "GetProductsResponse{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
