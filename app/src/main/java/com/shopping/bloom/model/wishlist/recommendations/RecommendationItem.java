package com.shopping.bloom.model.wishlist.recommendations;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecommendationItem {

    @Expose
    @SerializedName("id")
    private Integer id;
    @Expose
    @SerializedName("price")
    private String price;
    @Expose
    @SerializedName("primary_image")
    private String imagePath;
    @Expose
    @SerializedName("product_name")
    private String productName;

    public Integer getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getProductName() {
        return productName;
    }
}
