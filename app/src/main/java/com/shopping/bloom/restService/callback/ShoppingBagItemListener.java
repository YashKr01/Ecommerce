package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.CartItem;

public interface ShoppingBagItemListener {
    void removeCartItem(CartItem cartItem);
    void maxItemAdded();
    void updateCartItem(CartItem cartItem);
    void noInternet();
}
