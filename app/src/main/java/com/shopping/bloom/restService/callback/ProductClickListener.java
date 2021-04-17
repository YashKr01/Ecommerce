package com.shopping.bloom.restService.callback;

import com.shopping.bloom.models.Product;
import com.shopping.bloom.models.SubProduct;

public interface ProductClickListener {
    void onProductClick(Product productCategory);
    void onSubProductClick(SubProduct product);
}
