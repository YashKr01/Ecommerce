package com.shopping.bloom.model.recentlyviewed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecentlyViewedResponse {

    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private List<RecentlyViewedItem> recentlyViewedItemList;

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

    public List<RecentlyViewedItem> getRecentlyViewedItemList() {
        return recentlyViewedItemList;
    }

    public void setRecentlyViewedItemList(List<RecentlyViewedItem> recentlyViewedItemList) {
        this.recentlyViewedItemList = recentlyViewedItemList;
    }
}
