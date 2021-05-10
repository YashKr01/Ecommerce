package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.ResponseCartData;

public class GetCartValueResponse {

    @SerializedName("success")
    Boolean success;

    @SerializedName("data")
    ResponseCartData data;

    @SerializedName("message")
    String message;

    public GetCartValueResponse(Boolean success, ResponseCartData data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ResponseCartData getData() {
        return data;
    }

    public void setData(ResponseCartData data) {
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
        return "GetCartValueResponse{" +
                "success='" + success + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
