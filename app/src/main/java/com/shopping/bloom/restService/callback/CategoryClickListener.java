package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Category;
import com.shopping.bloom.model.SubCategory;

public interface CategoryClickListener {
    void onCategoryClicked(Category categoryCategory);
    void onSubCategoryClicked(SubCategory product);
}
