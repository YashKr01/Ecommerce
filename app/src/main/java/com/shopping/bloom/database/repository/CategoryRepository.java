package com.shopping.bloom.database.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.shopping.bloom.App;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.restService.response.GetCategoryResponse;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.LoginManager;

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
        int SUCCESS_CODE = 200;
        ApiInterface apiInterface = RetrofitBuilder.getInstance(context).getApi();
        String authToken = getToken();
        Log.d(TAG, "getCategory: authToken " + authToken);
        Call<GetCategoryResponse> responseCall = apiInterface.getCategory(
                authToken, mainCategory, limit, pageNo, categoryName
        );

        if(responseCall != null) {
            responseCall.enqueue(new Callback<GetCategoryResponse>() {
                @Override
                public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
                    if(!response.isSuccessful()){
                        //... return
                        responseListener.onFailure(-1, response.errorBody().toString());
                        return;
                    }

                    if(response.body() != null && response.code() == SUCCESS_CODE) {
                        GetCategoryResponse categoryResponse = response.body();
                        if(categoryResponse != null && categoryResponse.isSuccess()) {
                            responseListener.onSuccess(categoryResponse.getData());
                            return ;
                        }
                    }
                    responseListener.onFailure(response.code(), response.message());
                }

                @Override
                public void onFailure(Call<GetCategoryResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    responseListener.onFailure(-1, t.getMessage());
                }
            });
        }
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
