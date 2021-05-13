package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.ResponseCheckoutData;

public class GetCheckoutResponse {
    @SerializedName("success")
    Boolean success;

    @SerializedName("data")
    ResponseCheckoutData data;

    @SerializedName("message")
    String message;

    public GetCheckoutResponse(Boolean success, ResponseCheckoutData data, String message) {
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

    public ResponseCheckoutData getData() {
        return data;
    }

    public void setData(ResponseCheckoutData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
