package com.shopping.bloom.model.wishlist.recommendations;

public class RecommendationsItem {

    private Integer id;
    private String price;
    private String imagePath;

    public RecommendationsItem(Integer id, String price, String imagePath) {
        this.id = id;
        this.price = price;
        this.imagePath = imagePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
