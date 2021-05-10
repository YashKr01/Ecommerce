package com.shopping.bloom.model;

import com.google.gson.annotations.SerializedName;

public class PostCartProduct {

    @SerializedName("product_variable_id")
    String productId;

    @SerializedName("customer_qty")
    String quantity;

    public PostCartProduct(String productId, String quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
