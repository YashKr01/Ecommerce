package com.shopping.bloom.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.MainActivity;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.network.ApiServiceOtp;
import com.shopping.bloom.network.RetroInstance;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpViewModel extends ViewModel {

    LoginManager loginManager;

    public OtpViewModel(){ }


    public void makeApiCallVerifyOtp(OtpModel otpModel, Activity context) {
        loginManager = new LoginManager(context);
        ApiServiceOtp apiService = RetroInstance.getRetrofitClient().create(ApiServiceOtp.class);
        Call<OtpResponseModel> call = apiService.sendOtp(otpModel);
        call.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                if (response.isSuccessful()) {
                    String success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if(success.equals("true")) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        loginManager.setEmailid(response.body().getData().getUserInfo().getEmail());
                        loginManager.setname(response.body().getData().getUserInfo().getName());
                        loginManager.setNumber(response.body().getData().getUserInfo().getMobile_no());
                        loginManager.settoken(response.body().getData().getUserInfo().getFirebase_token());

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        context.finish();

                        loginManager.SetLoginStatus(false);
                    }else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Otp Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponseModel> call, Throwable t) {

            }
        });

    }

    public void makeApiCallResendOtp(LoginModel loginModel, Context context) {
        ApiServiceOtp apiService = RetroInstance.getRetrofitClient().create(ApiServiceOtp.class);
        Call<LoginResponseModel> call = apiService.resendOtp(loginModel);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

            }
        });

    }
}
