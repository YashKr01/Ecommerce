package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.wishlist.WishListData;

public interface WishListProductListener {
    void wishListItemDelete(WishListData wishListData, int position);

    void wishListItemCLicked(WishListData wishListData);

    void recentlyViewedOnClicked(RecentlyViewedItem recentlyViewedItem);
}
