package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Product;

public interface WishListListener {
    void updateWishList(int position ,boolean isAdded);
    void productClicked(Product product);
}
