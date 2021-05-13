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
import com.shopping.bloom.restService.callback.CheckoutResponseListener;
import com.shopping.bloom.restService.response.GetCategoryResponse;
import com.shopping.bloom.restService.response.PostCheckoutData;

import java.util.List;

public class ShoppingBagViewModel extends AndroidViewModel {

    private ShoppingBagRepository repository;
    private Application context;

    public ShoppingBagViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        this.repository = new ShoppingBagRepository();
    }

    public void getCartValue(List<PostCartProduct> postCartProducts, CartValueCallback mListener) {
        repository.getCartValue(context, postCartProducts, mListener);
    }

    public void getCheckoutData(PostCheckoutData checkoutData, CheckoutResponseListener listener) {
        repository.getCheckOutResponse(context, checkoutData, listener);
    }



    /*Room database*/
    public LiveData<List<CartItem>> getAllCartItem() {
        return  EcommerceDatabase.getInstance().cartItemDao().getAllCartItem();
    }

    /*Room database*/
    public void removeItemFromCart(CartItem cartItem) {
        EcommerceDatabase.databaseWriteExecutor.execute(()-> {
            EcommerceDatabase.getInstance().cartItemDao().removeItem(cartItem);
        });
    }

    /*Room database*/
    public void updateCartItem(CartItem cartItem) {
        EcommerceDatabase.databaseWriteExecutor.execute(()->{
            EcommerceDatabase.getInstance().cartItemDao().update(cartItem);
        });
    }

    /*Room database*/
    public LiveData<Integer> getTotalCartItems() {
        return EcommerceDatabase.getInstance().cartItemDao().changeCartIcon();
    }
}
