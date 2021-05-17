package com.shopping.bloom.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.MainActivity;
import com.shopping.bloom.model.SplashData;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.LoginWithPassResponseModel;
import com.shopping.bloom.restService.response.RefreshTokenResponse;
import com.shopping.bloom.restService.response.SplashBearerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends ViewModel {
    private final MutableLiveData<SplashData> splashDataMutableLiveData;
    private final MutableLiveData<RefreshTokenResponse> refreshTokenResponseMutableLiveData;

    public SplashViewModel() {
        splashDataMutableLiveData = new MutableLiveData<>();
        refreshTokenResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<SplashData> getSplashDataMutableLiveData() {
        return splashDataMutableLiveData;
    }

    public MutableLiveData<RefreshTokenResponse> getRefreshTokenResponseMutableLiveData(){
        return refreshTokenResponseMutableLiveData;
    }

    public void makeApiCall(String imei_number, Application application) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<SplashBearerResponse> call = apiService.sendBearerToken(imei_number);
        call.enqueue(new Callback<SplashBearerResponse>() {
            @Override
            public void onResponse(Call<SplashBearerResponse> call, Response<SplashBearerResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    splashDataMutableLiveData.postValue(response.body().getSplashData());
                }
            }

            @Override
            public void onFailure(Call<SplashBearerResponse> call, Throwable t) {
                splashDataMutableLiveData.postValue(null);
            }
        });
    }

    public void makeApiCallCheckToken(String token, Application application) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<RefreshTokenResponse> call = apiService.checkBearerToken("Bearer " + token);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {

                if (response.code() == 401) {
                    Toast.makeText(application, "Invalid Token", Toast.LENGTH_SHORT).show();
                } else {
                    if(response.isSuccessful() && response != null){
                        refreshTokenResponseMutableLiveData.postValue(response.body());
                    }
                }

            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                refreshTokenResponseMutableLiveData.postValue(null);
            }
        });
    }
}
