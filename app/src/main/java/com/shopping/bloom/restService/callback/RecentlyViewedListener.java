package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;

public interface RecentlyViewedListener {
    void recentItemClick(RecentlyViewedItem item);
}
