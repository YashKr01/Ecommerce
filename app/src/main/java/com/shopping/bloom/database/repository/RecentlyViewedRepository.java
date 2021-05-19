package com.shopping.bloom.database.repository;

import android.app.Application;
import android.util.Log;

import com.shopping.bloom.App;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentlyViewedRepository {
    private static final String TAG = RecentlyViewedRepository.class.getName();

    public void getRecentlyViewedList(Application context, int pageNo, int limit, ProductResponseListener responseListener) {
        String token = getToken();
        Call<GetProductsResponse> responseCall = RetrofitBuilder.getInstance(context).getApi()
                .getRecentlyViewedList(token, pageNo, limit);

        if(responseCall != null) {
            responseCall.enqueue(new Callback<GetProductsResponse>() {
                @Override
                public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                    if(!response.isSuccessful() || response.body() == null) {
                        responseListener.onFailure(-1, "Unsuccessful");
                        return;
                    }
                    if(response.code() == Const.SUCCESS) {
                        GetProductsResponse response1 = response.body();
                        if(response1 != null && response1.isSuccess()) {
                            responseListener.onSuccess(response1.getData());
                        } else {
                            responseListener.onFailure(Const.SUCCESS, response.message());
                        }
                    } else {
                        responseListener.onFailure(response.code(), "Unknown error");
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
