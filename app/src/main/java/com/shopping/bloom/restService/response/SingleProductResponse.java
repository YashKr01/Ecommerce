package com.shopping.bloom.restService.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.SingleProductDataResponse;

public class SingleProductResponse {

    @SerializedName("data")
    @Expose
    SingleProductDataResponse singleProductDataResponse;

    public SingleProductDataResponse getSingleProductDataResponse() {
        return singleProductDataResponse;
    }

    public void setSingleProductResponse(SingleProductDataResponse singleProductDataResponse) {
        this.singleProductDataResponse = singleProductDataResponse;
    }
}
