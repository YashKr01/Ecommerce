package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LoginResponseModel {
    @SerializedName("success")
    @Expose
    String success;

    @SerializedName("message")
    @Expose
    String message;

//    @SerializedName("data")
//    @Expose
//    ArrayList<Data> data;
//
//    public ArrayList<Data> getData() {
//        return data;
//    }
//
//    public void setData(ArrayList<Data> data) {
//        this.data = data;
//    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
