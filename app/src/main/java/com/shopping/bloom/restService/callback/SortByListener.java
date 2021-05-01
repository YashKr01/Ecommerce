package com.shopping.bloom.restService.callback;

import com.shopping.bloom.utils.Const;

public interface SortByListener {
    void onSortSelected(Const.SORT_BY sortBy, String value);
}
