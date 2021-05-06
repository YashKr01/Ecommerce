package com.shopping.bloom.restService.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.RandomImageDataResponse;

import java.util.List;

public class RandomImageResponse {

    @SerializedName("data")
    @Expose
    List<RandomImageDataResponse> imageDataResponseList;

    public List<RandomImageDataResponse> getImageDataResponseList() {
        return imageDataResponseList;
    }

    public void setImageDataResponseList(List<RandomImageDataResponse> imageDataResponseList) {
        this.imageDataResponseList = imageDataResponseList;
    }
}
