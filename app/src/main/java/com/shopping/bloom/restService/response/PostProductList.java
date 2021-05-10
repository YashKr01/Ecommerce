package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.PostCartProduct;

import java.util.List;

public class PostProductList {
    @SerializedName("products_list")
    List<PostCartProduct> productList;

    public PostProductList(List<PostCartProduct> productList) {
        this.productList = productList;
    }
}
