package com.shopping.bloom.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.database.repository.CategoryRepository;
import com.shopping.bloom.restService.callback.CategoryResponseListener;

public class CategoryViewModel extends AndroidViewModel {

    Application context;
    CategoryRepository repository;
    CategoryResponseListener responseListener;

    public CategoryViewModel(@NonNull Application context){
        super(context);
        this.context = context;
        repository = CategoryRepository.getInstance();
    }

    public void setResponseListener(CategoryResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void fetchData(String mainCategory, int limit, int pageNo, String categoryName) {
        repository.getCategory(mainCategory, limit, pageNo, categoryName, context, responseListener);
    }

}
