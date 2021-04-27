package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class ProductIds {

    @SerializedName("product_ids")
    @Expose
    private String productIds;

    public ProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }
}
