package com.shopping.bloom.restService.response;

import com.shopping.bloom.model.Product;

import java.util.List;

public class GetCategoryResponse {
    public boolean success;
    public List<Product> data;
    public String message;
}
