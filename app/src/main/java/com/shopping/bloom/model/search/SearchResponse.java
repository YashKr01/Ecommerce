package com.shopping.bloom.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @Expose
    @SerializedName("success")
    private Boolean success;
    @Expose
    @SerializedName("data")
    private List<SearchProduct> searchProducts;

    public Boolean getSuccess() {
        return success;
    }

    public List<SearchProduct> getSearchProducts() {
        return searchProducts;
    }
}
