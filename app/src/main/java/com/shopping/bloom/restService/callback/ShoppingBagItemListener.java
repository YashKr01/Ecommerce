package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.shoppingbag.ProductEntity;

public interface ShoppingBagItemListener {
    void btnRemoveClickListener(ProductEntity productEntity);
}
