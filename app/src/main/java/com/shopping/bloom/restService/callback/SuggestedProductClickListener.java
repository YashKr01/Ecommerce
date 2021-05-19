package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.ProductSuggestion;

public interface SuggestedProductClickListener {
    void onProductImageClicked(ProductSuggestion productSuggestion);
    void onProductAdd(ProductSuggestion productSuggestion, int position);
}
