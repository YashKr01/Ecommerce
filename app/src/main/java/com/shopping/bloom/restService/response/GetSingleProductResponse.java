package com.shopping.bloom.restService.response;

import com.shopping.bloom.model.ProductSuggestion;

import java.util.List;

public class GetSingleProductResponse {
    public boolean success;
    public List<ProductSuggestion> data;
    public String message;
}
