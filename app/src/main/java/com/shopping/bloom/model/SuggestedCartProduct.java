package com.shopping.bloom.model;

public class SuggestedCartProduct {
    String imageUrl;
    String productName;

    public SuggestedCartProduct(String imageUrl, String productName) {
        this.imageUrl = imageUrl;
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
