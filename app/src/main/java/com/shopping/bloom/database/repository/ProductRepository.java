package com.shopping.bloom.database.repository;

import android.app.Application;
import android.util.Log;

import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.response.GetProductsResponse;

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

    public void getProducts(Application context, String subCategory, int limit, ProductResponseListener responseListener) {
        Log.d(TAG, "getProducts: subCategory: " + subCategory + " limit: " + limit);

        ApiInterface apiInterface = RetrofitBuilder.getInstance(context).getApi();

        Call<GetProductsResponse> responseCall = apiInterface.getProducts(subCategory, limit);

        Log.d(TAG, "getProducts: Request " + responseCall.request().toString());

        if (responseCall != null) {
            responseCall.enqueue(new Callback<GetProductsResponse>() {
                @Override
                public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                    if (!response.isSuccessful()) {
                        //... return
                        responseListener.onFailure(-1, response.errorBody().toString());
                        return;
                    }

                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: response body" + response.body().toString());
                        GetProductsResponse productsResponse = response.body();
                        Log.d(TAG, "onResponse: productsResponse " + productsResponse.toString());
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


}
