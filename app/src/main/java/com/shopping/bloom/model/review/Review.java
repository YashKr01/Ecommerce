package com.shopping.bloom.model.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @Expose
    @SerializedName("created_at")
    private String name;
    @Expose
    @SerializedName("comment")
    private String review;
    @Expose
    @SerializedName("rating")
    private String rating;

    public Review() {
    }

    public Review(String name, String review, String rating) {
        this.name = name;
        this.review = review;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Review{" +
                "name='" + name + '\'' +
                ", review='" + review + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
