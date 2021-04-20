package com.shopping.bloom.model.fragmentnew;

import com.shopping.bloom.model.Product;

import java.util.List;

public class NewTrend {

    private String imageUrl;
    private String title;
    private String description;
    private int background;
    private List<Product> productList;

    public NewTrend() {
    }

    public NewTrend(String imageUrl, String title, String description, int background, List<Product> productList) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.background = background;
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
