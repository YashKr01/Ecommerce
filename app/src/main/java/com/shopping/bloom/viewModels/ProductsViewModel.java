package com.shopping.bloom.viewModels;

import android.app.Application;
import android.content.Intent;
import android.telephony.ims.ImsMmTelManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.database.dao.WishListProductDao;
import com.shopping.bloom.database.repository.ProductRepository;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.ProductFilter;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.callback.ProductResponseListener;

import java.util.List;

public class ProductsViewModel extends AndroidViewModel {

    private static final String TAG = ProductsViewModel.class.getName();

    Application context;
    ProductRepository repository;
    ProductResponseListener responseListener;
    WishListProductDao wishListProductDao;
    private int ITEM_LIMIT = 20;        // Maximum items in single API call

    public ProductsViewModel(@NonNull Application context) {
        super(context);
        this.context = context;
        repository = ProductRepository.getInstance();
        wishListProductDao = EcommerceDatabase.getInstance().wishListProductDao();
    }

    public void setResponseListener(ProductResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void fetchData(String categoryId, int pageNo, ProductFilter filter) {
        repository.getProducts(context, categoryId,ITEM_LIMIT, pageNo, filter, responseListener);
    }

    public void addToWishList(WishListItem wishListItem) {
        EcommerceDatabase.databaseWriteExecutor.execute(()->{
            wishListProductDao.addToWishList(wishListItem);
        });
    }

    public void removeFromWishList(WishListItem wishListItem) {
        EcommerceDatabase.databaseWriteExecutor.execute(()->{
            wishListProductDao.delete(wishListItem);
        });
    }

}
