package com.shopping.bloom.database.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.restService.response.GetCategoryResponse;
import com.shopping.bloom.utils.Const;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private static final String TAG = "CategoryRepository";

    private static CategoryRepository repository;

    public static CategoryRepository getInstance() {
        if (repository == null) {
            repository = new CategoryRepository();
        }
        return repository;
    }

    public void getCategory(String mainCategory, int limit, int pageNo, String categoryName, Application context, CategoryResponseListener responseListener) {
        Log.d(TAG, "getCategory: mainCategory=" + mainCategory + " limit=" + limit + " pageNo=" + pageNo + " categoryName=" + categoryName);

        ApiInterface apiInterface = RetrofitBuilder.getInstance(context).getApi();

        Call<GetCategoryResponse> responseCall = apiInterface.getCategory(
                mainCategory, limit, pageNo, categoryName
        );

        Log.d(TAG, "getCategory: REQUEST "+ responseCall.request().toString());

        if(responseCall != null) {
            responseCall.enqueue(new Callback<GetCategoryResponse>() {
                @Override
                public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
                    if(!response.isSuccessful()){
                        //... return
                        responseListener.onFailure(-1, response.errorBody().toString());
                        return;
                    }

                    if(response.body() != null) {
                        Log.d(TAG, "onResponse: response body"+ response.body().toString());
                        GetCategoryResponse categoryResponse = response.body();
                        Log.d(TAG, "onResponse: categoryResponse "+ categoryResponse.toString());
                        responseListener.onSuccess(categoryResponse.getData());
                    } else {
                        responseListener.onFailure(response.code(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<GetCategoryResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    responseListener.onFailure(-1, t.getMessage());
                }
            });
        }
    }

}
