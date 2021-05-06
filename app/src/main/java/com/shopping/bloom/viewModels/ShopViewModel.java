package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.shopping.bloom.database.repository.CategoryRepository;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.restService.callback.ProductResponseListener;

public class ShopViewModel extends AndroidViewModel {

    Application context;
    CategoryRepository repository;
    CategoryResponseListener responseListener;
    ProductResponseListener randomProductListener;
    ProductResponseListener recommendedProductListener;

    public ShopViewModel(@NonNull Application context){
        super(context);
        this.context = context;
        repository = CategoryRepository.getInstance();
    }

    public void setResponseListener(CategoryResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void setRandomProductListener(ProductResponseListener randomProductListener) {
        this.randomProductListener = randomProductListener;
    }

    public void setRecommendedProductListener(ProductResponseListener recommendedProductListener) {
        this.recommendedProductListener = recommendedProductListener;
    }


    /*
    *   fetch all the category Items
    * */
    public void fetchCategoryItems(String mainCategory, int limit, int pageNo, String categoryName) {
        repository.getCategory(mainCategory, limit, pageNo, categoryName, context, responseListener);
    }

    public void fetchRandomProduct(int pageNo, int limit) {
        repository.getRandomProduct(context, pageNo, limit, randomProductListener);
    }

    public void fetchRecommendProduct(int pageNo, int limit) {
        repository.getRecommendedProducts(context, pageNo, limit, recommendedProductListener);
    }


}

















