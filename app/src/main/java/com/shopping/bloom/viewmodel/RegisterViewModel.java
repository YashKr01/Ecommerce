package com.shopping.bloom.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.OtpActivity;
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

    public void makeApiCall(RegistrationModel registrationModel, Context context){
        ApiServiceRegistration apiService = RetroInstance.getRetrofitClient().create(ApiServiceRegistration.class);
        Call<RegisterResponseModel> call = apiService.sendRegisterData(registrationModel);
        call.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                if(response.isSuccessful()){
                    String success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if(success.equals("true")){
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent( context, OtpActivity.class);
                        intent.putExtra("mobile_no",registrationModel.getMobile_no());
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {

            }
        });

    }
}
