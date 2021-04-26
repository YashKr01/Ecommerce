package com.shopping.bloom.model.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @Expose
    @SerializedName("comment")
    private String review;
    @Expose
    @SerializedName("rating")
    private String rating;
    @Expose
    @SerializedName("user_info")
    private UserInfo userInfo;

    public Review() {
    }

    public Review(String review, String rating,UserInfo userInfo) {
        this.review = review;
        this.rating = rating;
        this.userInfo=userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
