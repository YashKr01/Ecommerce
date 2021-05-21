package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.FilterItem;

public interface FilterClickListener {
    void onFilterApplied(FilterItem filter);
}
