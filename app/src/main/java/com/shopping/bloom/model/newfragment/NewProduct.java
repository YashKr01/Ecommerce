package com.shopping.bloom.model.newfragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewProduct {

    @Expose
    @SerializedName("id")
    private Integer id;
    @Expose
    @SerializedName("price")
    private String price;
    @Expose
    @SerializedName("primary_image")
    private String imagePath;

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
