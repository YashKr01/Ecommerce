package com.shopping.bloom.model.review;

public class Review {

    private String name;
    private String review;
    private int rating;

    public Review() {
    }

    public Review(String name, String review, int rating) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
