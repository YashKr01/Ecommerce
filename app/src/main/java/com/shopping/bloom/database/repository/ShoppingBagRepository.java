package com.shopping.bloom.database.repository;

import android.app.Application;
import android.util.Log;

import com.shopping.bloom.App;
import com.shopping.bloom.model.PostCartProduct;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.CartValueCallback;
import com.shopping.bloom.restService.response.GetCartValueResponse;
import com.shopping.bloom.restService.response.PostProductList;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingBagRepository {

    private static final String TAG = "ShoppingBagRepository";
    private final int SUCCESS = 200;


    public void getCartValue(Application context, List<PostCartProduct> postCartProducts, CartValueCallback mListener) {
        ApiInterface apiInterface = RetrofitBuilder.getInstance(context).getApi();
        String authToken = getToken();
        Log.d(TAG, "getCartValue: Token " + authToken);
        PostProductList productList = new PostProductList(postCartProducts);
        Call<GetCartValueResponse> responseCall =
                apiInterface.getCartValue(authToken, productList);

        if (responseCall != null) {
            responseCall.enqueue(new Callback<GetCartValueResponse>() {
                @Override
                public void onResponse(Call<GetCartValueResponse> call, Response<GetCartValueResponse> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        mListener.onFailed(response.code(), response.message());
                        return;
                    }
                    if (response.code() != SUCCESS) {
                        mListener.onFailed(response.code(), response.message());
                        return;
                    }
                    GetCartValueResponse response1 = response.body();
                    if (response1.getSuccess()) {
                        mListener.onSuccess(response1);
                    } else {
                        mListener.onFailed(SUCCESS, "Response is successful but Invalid error from server");
                    }
                }

                @Override
                public void onFailure(Call<GetCartValueResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");
                    mListener.onFailed(-1, t.getMessage());
                }
            });
        }

    }

    public static String getToken() {
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
