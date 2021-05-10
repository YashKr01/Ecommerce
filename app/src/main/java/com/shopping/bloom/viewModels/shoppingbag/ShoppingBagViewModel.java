package com.shopping.bloom.viewModels.shoppingbag;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.database.repository.ShoppingBagRepository;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.model.PostCartProduct;
import com.shopping.bloom.model.shoppingbag.ProductEntity;
import com.shopping.bloom.restService.callback.CartValueCallback;
import com.shopping.bloom.restService.response.GetCategoryResponse;

import java.util.List;

public class ShoppingBagViewModel extends AndroidViewModel {

    private ShoppingBagRepository repository;
    private Application context;

    public ShoppingBagViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        this.repository = new ShoppingBagRepository();
    }

    public LiveData<List<CartItem>> getAllCartItem() {
        return  EcommerceDatabase.getInstance().cartItemDao().getAllCartItem();
    }

    public void removeItemFromCart(CartItem cartItem) {
        EcommerceDatabase.databaseWriteExecutor.execute(()-> {
            EcommerceDatabase.getInstance().cartItemDao().removeItem(cartItem);
        });
    }

    public void getCartValue(List<PostCartProduct> postCartProducts, CartValueCallback mListener) {
        repository.getCartValue(context, postCartProducts, mListener);
    }
}
