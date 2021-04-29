package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.FilterArrayValues;

import java.util.List;

/*
*
* {
    "success": true,
    "data": {
        "colorData": [
            "Medium",
            "Large",
            "Small"
        ],
        "sizeData": [
            "Blue",
            "Black",
            "Red",
            "Brown"
        ]
    },
    "message": "Data Fetched Successfully"
    }
* */

public class GetColorAndSizeResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private FilterArrayValues filterArrays;

    @SerializedName("message")
    private String message;

    public GetColorAndSizeResponse(boolean success, FilterArrayValues filterArrays, String message) {
        this.success = success;
        this.filterArrays = filterArrays;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FilterArrayValues getFilterArrays() {
        return filterArrays;
    }

    public void setFilterArrays(FilterArrayValues filterArrays) {
        this.filterArrays = filterArrays;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GetColorAndSizeResponse{" +
                "success=" + success +
                ", filterArrays=" + filterArrays +
                ", message='" + message + '\'' +
                '}';
    }
}
