package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Product;

import java.util.List;

public interface ProductResponseListener {

    void onSuccess(List<Product> products);
    void onFailure(int errorCode, String message);
}
