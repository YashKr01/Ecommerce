package com.shopping.bloom.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchProduct {

    @Expose
    @SerializedName("id")
    private Integer id;
    @Expose
    @SerializedName("product_name")
    private String productName;
    @Expose
    @SerializedName("primary_image")
    private String productImage;

    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

}
