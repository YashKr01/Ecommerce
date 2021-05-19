package com.shopping.bloom.viewModels;

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
import com.shopping.bloom.restService.callback.SingleProductCallback;
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

    public void fetchSuggestedProduct(int pageNo, int limit, SingleProductCallback callback) {
        repository.fetchSingleProductSuggestion(context, pageNo, limit, callback);
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

    /*
    *   FIRST CHECK IF THE ITEM ALREADY PRESENT IN THE CART OR NOT
    *   if already present then check for the quantity
    * */
    public void addToCart(CartItem cartItem, int maxQuantity) {
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            List<CartItem> cartItems = EcommerceDatabase.getInstance().cartItemDao()
                    .getAllProductWith(cartItem.getParentId(), cartItem.getChildId());
            if (cartItems == null || cartItems.isEmpty()) {
                EcommerceDatabase.getInstance().cartItemDao().addToCart(cartItem);
            } else {
                if (cartItems.get(0).getQuantity() < maxQuantity) {
                    EcommerceDatabase.getInstance().cartItemDao()
                            .incrementQuantity(cartItem.getParentId(), cartItem.getChildId());
                }
            }
        });
    }
}
