package com.shopping.bloom.restService.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.AddressDataResponse;

import java.util.List;

public class AddressResponse {

    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("data")
    @Expose
    List<AddressDataResponse> addressResponseList;

    @SerializedName("message")
    @Expose
    String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<AddressDataResponse> getAddressResponseList() {
        return addressResponseList;
    }

    public void setAddressResponseList(List<AddressDataResponse> addressResponseList) {
        this.addressResponseList = addressResponseList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
