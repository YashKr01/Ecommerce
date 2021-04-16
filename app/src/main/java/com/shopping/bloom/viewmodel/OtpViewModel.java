package com.shopping.bloom.viewmodel;

import androidx.lifecycle.ViewModel;

import com.shopping.bloom.model.LoginResponseModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.model.OtpResponseModel;
import com.shopping.bloom.network.ApiServiceLogin;
import com.shopping.bloom.network.ApiServiceOtp;
import com.shopping.bloom.network.RetroInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpViewModel extends ViewModel {

    public OtpViewModel() {

    }

    public void makeApiCallVerifyOtp(OtpModel otpModel){
        ApiServiceOtp apiService = RetroInstance.getRetrofitClient().create(ApiServiceOtp.class);
        Call<OtpResponseModel> call = apiService.sendOtp(otpModel);
        call.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<OtpResponseModel> call, Throwable t) {

            }
        });

    }

    public void makeApiCallResendOtp(String mobile_no){
        ApiServiceOtp apiService = RetroInstance.getRetrofitClient().create(ApiServiceOtp.class);
        Call<OtpResponseModel> call = apiService.resendOtp(mobile_no);
        call.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<OtpResponseModel> call, Throwable t) {

            }
        });

    }
}
