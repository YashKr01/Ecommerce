package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.App;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.restService.response.SingleProductResponse;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleProductViewModel extends ViewModel {

    private final MutableLiveData<SingleProductDataResponse> mutableLiveData;

    public SingleProductViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<SingleProductDataResponse> getMutableLiveData() {
        return mutableLiveData;
    }

    public void makeApiCall(Application application) {

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<SingleProductResponse> call = apiService.getSingleProduct("Bearer " + token);
        call.enqueue(new Callback<SingleProductResponse>() {
            @Override
            public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                mutableLiveData.postValue(response.body().getSingleProductDataResponse());
            }

            @Override
            public void onFailure(Call<SingleProductResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }
}
