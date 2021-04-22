package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.Category;

import java.util.List;

public interface CategoryResponseListener {
    void onSuccess(List<Category> category);
    void onFailure(int errorCode, String errorMessage);
}
