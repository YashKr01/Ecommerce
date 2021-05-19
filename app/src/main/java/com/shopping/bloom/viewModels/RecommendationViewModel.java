package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.model.wishlist.recommendations.RecommendationItem;
import com.shopping.bloom.model.wishlist.recommendations.RecommendationResponse;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationViewModel extends AndroidViewModel {

    private Application application;

    public RecommendationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void getRecommendationList(int limit, int page, ProductResponseListener responseListener) {
        String token = getToken();
        Call<GetProductsResponse> responseCall = RetrofitBuilder.getInstance(application).getApi()
                    .getRandomProducts(token, page, limit);

        if(responseCall != null) {
            responseCall.enqueue(new Callback<GetProductsResponse>() {
                @Override
                public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                    if(!response.isSuccessful() || response.body() == null) {
                        responseListener.onFailure(-1, "Something went wrong");
                    }
                    if(response.code() == Const.SUCCESS) {
                        GetProductsResponse categoryResponse = response.body();
                        if(categoryResponse != null) {
                            responseListener.onSuccess(categoryResponse.getData());
                        } else {
                            responseListener.onFailure(200, "Invalid data received");
                        }
                    } else {
                            responseListener.onFailure(response.code(), "unrecognised error code");
                    }
                }

                @Override
                public void onFailure(Call<GetProductsResponse> call, Throwable t) {
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
