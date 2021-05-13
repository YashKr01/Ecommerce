package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.Promocode;

import java.util.List;

public class GetAvailablePromoResponse {

    @SerializedName("success")
    String success;

    @SerializedName("data")
    List<Promocode> promocodes;

    @SerializedName("message")
    String message;

    public GetAvailablePromoResponse(String success, List<Promocode> promocodes, String message) {
        this.success = success;
        this.promocodes = promocodes;
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public List<Promocode> getPromocodes() {
        return promocodes;
    }

    public String getMessage() {
        return message;
    }

}
