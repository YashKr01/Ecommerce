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
import com.shopping.bloom.restService.response.SplashBearerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends ViewModel {
    private final MutableLiveData<SplashData> splashDataMutableLiveData;

    public SplashViewModel(){
        splashDataMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<SplashData> getSplashDataMutableLiveData(){
        return splashDataMutableLiveData;
    }
    public void makeApiCall(String imei_number, Application application){
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<SplashBearerResponse> call = apiService.sendBearerToken(imei_number);
        call.enqueue(new Callback<SplashBearerResponse>() {
            @Override
            public void onResponse(Call<SplashBearerResponse> call, Response<SplashBearerResponse> response) {
                splashDataMutableLiveData.postValue(response.body().getSplashData());
                String data = response.body().getSplashData().getToken();
                String d = response.toString();


                System.out.println(data);
                System.out.println(d);
            }

            @Override
            public void onFailure(Call<SplashBearerResponse> call, Throwable t) {
                System.out.println(call.request());
                System.out.println(t.toString());
            }
        });
    }
}