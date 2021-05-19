package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.ProductSuggestion;

import java.util.List;

/*
*   Callback interface for the
*      product suggestion below the cart items in the shopping bag
* */
public interface SingleProductCallback {
    void onSuccess(List<ProductSuggestion> productSuggestion);
    void onFailed(int errorCode, String errorMessage);
}
