package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.SubProduct;

public interface ProductClickListener {
    void onProductClick(Product productCategory);
    void onSubProductClick(SubProduct product);
}
