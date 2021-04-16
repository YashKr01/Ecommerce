package com.shopping.bloom.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.model.RegisterResponseModel;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.network.ApiServiceRegistration;
import com.shopping.bloom.network.RetroInstance;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    public RegisterViewModel() {

    }

    public void makeApiCall(RegistrationModel registrationModel){
        ApiServiceRegistration apiService = RetroInstance.getRetrofitClient().create(ApiServiceRegistration.class);
        Call<RegisterResponseModel> call = apiService.sendRegisterData(registrationModel);
        call.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {

            }
        });

    }
}
