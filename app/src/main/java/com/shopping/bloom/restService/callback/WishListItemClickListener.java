package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Product;

public interface WishListItemClickListener {
    void onItemClick(Product wishListItem);
    void onItemDelete(Product wishListItem, int position);
}
