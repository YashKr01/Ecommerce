package com.shopping.bloom.database.repository;

import androidx.lifecycle.LiveData;

import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.model.shoppingbag.ProductEntity;

import java.util.List;

public class ShoppingBagRepository {

    public LiveData<List<ProductEntity>> getShoppingBagItems() {

        return EcommerceDatabase.getInstance().wishListProductDao().getShoppingBagItems();
    }

    public void deleteFromShoppingBag(ProductEntity productEntity) {
        EcommerceDatabase.databaseWriteExecutor.execute(() ->
                EcommerceDatabase.getInstance().wishListProductDao().deleteShoppingBagItem(productEntity)
        );
    }

}
