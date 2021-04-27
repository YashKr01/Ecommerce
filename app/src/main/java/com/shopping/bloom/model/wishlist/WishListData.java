package com.shopping.bloom.model.wishlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishListData {

    @Expose
    @SerializedName("id")
    private Integer id;
    @Expose
    @SerializedName("product_name")
    private String productName;
    @Expose
    @SerializedName("price")
    private String price;
    @Expose
    @SerializedName("primary_image")
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
