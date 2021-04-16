package com.shopping.bloom.viewmodel;

import androidx.lifecycle.ViewModel;

import com.shopping.bloom.model.LoginResponseModel;
import com.shopping.bloom.network.ApiServiceLogin;
import com.shopping.bloom.network.RetroInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public LoginViewModel() {

    }

    public void makeApiCall(String mobile_no){
        ApiServiceLogin apiService = RetroInstance.getRetrofitClient().create(ApiServiceLogin.class);
        Call<LoginResponseModel> call = apiService.sendLoginData(mobile_no);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

            }
        });

    }
}
