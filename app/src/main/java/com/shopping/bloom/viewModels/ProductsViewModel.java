package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.shopping.bloom.database.repository.ProductRepository;
import com.shopping.bloom.restService.callback.ProductResponseListener;

public class ProductsViewModel extends AndroidViewModel {

    private static final String TAG = ProductsViewModel.class.getName();

    Application context;
    ProductRepository repository;
    ProductResponseListener responseListener;

    public ProductsViewModel(@NonNull Application context){
        super(context);
        this.context = context;
        repository = ProductRepository.getInstance();
    }

    public void setResponseListener(ProductResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void fetchData(String subCategoryId, int limit) {
        repository.getProducts(context, subCategoryId, limit, responseListener);
    }

}
