package com.shopping.bloom.model.wishlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishList {

    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<WishListData> wishListData;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WishListData> getWishListData() {
        return wishListData;
    }

    public void setWishListData(List<WishListData> wishListData) {
        this.wishListData = wishListData;
    }
}
