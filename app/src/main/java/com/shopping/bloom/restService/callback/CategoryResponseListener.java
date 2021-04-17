package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Product;

import java.util.List;

public interface CategoryResponseListener {
    void onSuccess(List<Product> product);
    void onFailure(int errorCode, String errorMessage);
}
