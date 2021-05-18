package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.LoginResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginResponseModel> mutableLiveData;

    public LoginViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponseModel> getMutableLiveData() {
        return mutableLiveData;
    }


    public void makeApiCall(LoginModel loginModel, Application application) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call = apiService.sendLoginData(loginModel);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });

    }
}
