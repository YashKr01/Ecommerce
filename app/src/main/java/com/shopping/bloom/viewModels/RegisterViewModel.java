package com.shopping.bloom.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.LoginActivity;
import com.shopping.bloom.activities.OtpActivity;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.restService.response.RegisterResponseModel;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.utils.NetworkCheck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<RegisterResponseModel> registerLiveData;

    public RegisterViewModel() {
        registerLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<RegisterResponseModel> getMutableLiveData() {
        return registerLiveData;
    }

    public void makeApiCall(RegistrationModel registrationModel, Application application, Activity context){

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<RegisterResponseModel> call = apiService.sendRegisterData(registrationModel);
        call.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                System.out.println(response);
                if(response.isSuccessful() && response.body() != null){
                    registerLiveData.postValue(response.body());
                }
                if(response.code() == 412) {
                    Toast.makeText(context, "Number or Email already been taken", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    context.finish();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                registerLiveData.postValue(null);
                Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });

    }
}
