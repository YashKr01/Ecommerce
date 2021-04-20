package com.shopping.bloom.model.fragmentshop;

public class RecentlyViewed {

    private String imageUrl;
    private int background;
    private int price;

    public RecentlyViewed() {
    }

    public RecentlyViewed(String imageUrl, int background, int price) {
        this.imageUrl = imageUrl;
        this.background = background;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
