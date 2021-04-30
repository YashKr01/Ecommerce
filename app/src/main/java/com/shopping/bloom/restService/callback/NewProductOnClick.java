package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.newfragment.NewProduct;
import com.shopping.bloom.model.newfragment.NewProductCategory;

public interface NewProductOnClick {
    void newProductListener(NewProduct newProduct);
    void newBannerListener(NewProductCategory newProductCategory);
}
