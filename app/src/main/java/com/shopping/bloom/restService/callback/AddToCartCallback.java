package com.shopping.bloom.restService.callback;

public interface AddToCartCallback {
    void onAdded(int totalItems);
    void onItemLimitReached(int maxItems);
}
