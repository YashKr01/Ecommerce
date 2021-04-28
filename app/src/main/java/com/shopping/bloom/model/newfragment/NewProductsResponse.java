package com.shopping.bloom.model.newfragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewProductsResponse {

    @Expose
    @SerializedName("data")
    private List<NewProductCategory> newCategoryList;
    @Expose
    @SerializedName("message")
    private String message;

    public List<NewProductCategory> getNewCategoryList() {
        return newCategoryList;
    }

    public void setNewCategoryList(List<NewProductCategory> newCategoryList) {
        this.newCategoryList = newCategoryList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
