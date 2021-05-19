package com.shopping.bloom.restService.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.AddressDataResponse;

import java.util.List;

public class AddAddressResponse {

    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("data")
    @Expose
    List<AddressDataResponse> addressDataResponseList;

    @SerializedName("message")
    @Expose
    String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<AddressDataResponse> getAddressDataResponseList() {
        return addressDataResponseList;
    }

    public void setAddressDataResponseList(List<AddressDataResponse> addressDataResponseList) {
        this.addressDataResponseList = addressDataResponseList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
