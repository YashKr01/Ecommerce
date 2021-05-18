package com.shopping.bloom.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.OtpActivity;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.restService.response.EmailVerificationResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.LoginResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSecurityViewModel extends ViewModel {
    private final MutableLiveData<EmailVerificationResponse> mutableLiveData;

    public AccountSecurityViewModel(){
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<EmailVerificationResponse> getMutableLiveData() {
        return mutableLiveData;
    }

    public void verifyEmailApiCall(EmailVerificationModel emailVerificationModel, Application application, Activity context){

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<EmailVerificationResponse> call = apiService.sendEmailVerifyData(emailVerificationModel);
        call.enqueue(new Callback<EmailVerificationResponse>() {
            @Override
            public void onResponse(Call<EmailVerificationResponse> call, Response<EmailVerificationResponse> response) {
                System.out.println(response);
                if(response.isSuccessful() && response.body() != null){
                    mutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
                Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });

    }

}
