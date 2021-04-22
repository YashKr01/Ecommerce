package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Category;
import com.shopping.bloom.model.SubCategory;

public interface ProductClickListener {
    void onProductClick(Category categoryCategory);
    void onSubProductClick(SubCategory product);
}
