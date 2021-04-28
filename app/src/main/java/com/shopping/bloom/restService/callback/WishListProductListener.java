package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.wishlist.WishListData;

public interface WishListProductListener {
    void wishListItemDelete(WishListData wishListData,int position);
}
