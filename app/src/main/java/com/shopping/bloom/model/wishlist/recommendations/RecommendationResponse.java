package com.shopping.bloom.model.wishlist.recommendations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendationResponse {

    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("data")
    private List<RecommendationItem> recommendationItemList;

    public boolean getSuccess() {
        return success;
    }

    public List<RecommendationItem> getRecommendationsItemList() {
        return recommendationItemList;
    }
}
