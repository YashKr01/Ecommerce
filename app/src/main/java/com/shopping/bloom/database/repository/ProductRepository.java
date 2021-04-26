package com.shopping.bloom.database.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.shopping.bloom.App;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.restService.response.PutWishListRequest;
import com.shopping.bloom.utils.LoginManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private static final String TAG = ProductRepository.class.getName();

    private static ProductRepository repository;

    public static ProductRepository getInstance() {
        if (repository == null) {
            repository = new ProductRepository();
        }
        return repository;
    }

    public void getProducts(Application context, String subCategory, int limit, int pageNo, ProductResponseListener responseListener) {
        Log.d(TAG, "getProducts: subCategory: " + subCategory + " limit: " + limit);

        ApiInterface apiInterface = RetrofitBuilder.getInstance(context).getApi();
        String authToken = getToken();      //TODO : add auth token method
        Call<GetProductsResponse> responseCall = apiInterface.getProducts(authToken, subCategory, limit, pageNo);

        Log.d(TAG, "getProducts: Request " + responseCall.request().toString());

        if (responseCall != null) {
            responseCall.enqueue(new Callback<GetProductsResponse>() {
                @Override
                public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "onResponse: response isn't successful message " + response.message());
                        responseListener.onFailure(-1, response.errorBody().toString());
                        return;
                    }

                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: response body" + response.body().toString());
                        GetProductsResponse productsResponse = response.body();
                        insertItems(productsResponse.getData());
                        responseListener.onSuccess(productsResponse.getData());
                    } else {
                        responseListener.onFailure(response.code(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<GetProductsResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    responseListener.onFailure(-1, t.getMessage());
                }
            });
        }

    }

    /*
    *  Insert all liked items into DB items into Database
    * */
    private void insertItems(List<Product> products) {
        if(products == null || products.size() == 0) return ;

        List<WishListItem> wishListItems = new ArrayList<>();
        String token = getToken();
        for(Product product: products) {
            if(product.isInUserWishList()) {
                WishListItem wishListItem = new WishListItem(String.valueOf(product.getId()), token);
                wishListItems.add(wishListItem);
            }
        }

        //insert into db
        EcommerceDatabase.databaseWriteExecutor.execute(()-> EcommerceDatabase.getInstance().wishListProductDao().addAllItems(wishListItems));
    }

    /**
     * Post request network call to save all wishList items
     *  to server and clean the database
     */
    public void uploadAutomationMessages(Application context) {
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            List<String> products = EcommerceDatabase.getInstance().wishListProductDao().getAllItem();
            if (products != null && products.size() > 0) {
                Log.d(TAG, "uploadAutomationMessages: " + products.toString());
                String authToken = getToken();
                ApiInterface apiInterface = RetrofitBuilder.getInstance(context).getApi();
                Call<PutWishListRequest> request = apiInterface.postUserWishList(authToken, products);
                saveMessagesOnServer(request);
            }
        });
    }

    private void saveMessagesOnServer(Call<PutWishListRequest> request) {
        if(request != null) {
            request.enqueue(new Callback<PutWishListRequest>() {
                @Override
                public void onResponse(Call<PutWishListRequest> call, Response<PutWishListRequest> response) {
                    Log.d(TAG, "onResponse: Removing all the items from the local DB");
                    deleteAll();
                }

                @Override
                public void onFailure(Call<PutWishListRequest> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    //Clean the database
    private void deleteAll() {
        EcommerceDatabase.databaseWriteExecutor.execute(()->{
            EcommerceDatabase.getInstance().wishListProductDao().deleteAll();
        });
    }

    private String getToken() {
        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }
        return "Bearer " + token;
    }


}
