package com.shopping.bloom.viewModels.shoppingbag;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shopping.bloom.database.repository.ShoppingBagRepository;
import com.shopping.bloom.model.shoppingbag.ProductEntity;

import java.util.List;

public class ShoppingBagViewModel extends AndroidViewModel {

    private ShoppingBagRepository repository;

    public ShoppingBagViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ShoppingBagRepository();
    }

    public LiveData<List<ProductEntity>> getShoppingBagItems() {
        return repository.getShoppingBagItems();
    }

    public void deleteFromShoppingBag(ProductEntity productEntity) {
        repository.deleteFromShoppingBag(productEntity);
    }

}
