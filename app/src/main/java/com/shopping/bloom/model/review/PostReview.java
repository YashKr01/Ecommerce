package com.shopping.bloom.model.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostReview {

    @Expose
    @SerializedName("product_id")
    private String productId;
    @Expose
    @SerializedName("comment")
    private String review;
    @Expose
    @SerializedName("rating")
    private String rating;

    public PostReview(String productId, String review, String rating) {
        this.productId = productId;
        this.review = review;
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
