package com.shopping.bloom.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.LoginActivity;
import com.shopping.bloom.activities.MainActivity;
import com.shopping.bloom.activities.SettingsActivity;
import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.EmailVerificationResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpViewModel extends ViewModel {

    LoginManager loginManager;

    public OtpViewModel() {
    }


    public void makeApiCallVerifyOtp(OtpModel otpModel, Application application, Activity context, String ActivityName) {
        loginManager = new LoginManager(context);
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<OtpResponseModel> call = apiService.sendOtp(otpModel);
        call.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    String success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success.equals("true")) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        loginManager.setEmailid(response.body().getData().getUserInfo().getEmail());
                        loginManager.setname(response.body().getData().getUserInfo().getName());
                        loginManager.setNumber(response.body().getData().getUserInfo().getMobile_no());
                        loginManager.setFirebase_token(response.body().getData().getUserInfo().getFirebase_token());
                        loginManager.settoken(response.body().getData().getToken());
                        loginManager.SetLoginStatus(false);
                        String email_verified_at = response.body().getData().getUserInfo().getEmail_verified_at();
                        if(email_verified_at != null || !email_verified_at.isEmpty()|| !email_verified_at.equals("")){
                            loginManager.setEmail_verified_at(true);
                        }

                        Intent intent;
                        if (ActivityName.equals("RegisterActivity")) {
                            intent = new Intent(context, LoginActivity.class);
                        }
                        else {
                            intent = new Intent(context, MainActivity.class);
                            loginManager.SetLoginStatus(false);
                        }
                        context.startActivity(intent);
                        context.finish();

                    } else {
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

    public void makeApiCallVerifyEmailOtp(EmailOtpModel otpModel, Application application, Activity context, String ActivityName) {
        loginManager = new LoginManager(context);
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<OtpResponseModel> call = apiService.verifyEmailOtp(otpModel);
        call.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    String success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success.equals("true")) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                        loginManager.setEmailid(response.body().getData().getUserInfo().getEmail());
                        loginManager.setname(response.body().getData().getUserInfo().getName());
                        loginManager.setNumber(response.body().getData().getUserInfo().getMobile_no());
                        loginManager.setFirebase_token(response.body().getData().getUserInfo().getFirebase_token());
                        loginManager.settoken(response.body().getData().getToken());
                        String email_verified_at = response.body().getData().getUserInfo().getEmail_verified_at();
                        if(email_verified_at != null || !email_verified_at.isEmpty()|| !email_verified_at.equals("")){
                            loginManager.setEmail_verified_at(true);
                        }

                        Intent intent = new Intent(context, SettingsActivity.class);
                        context.startActivity(intent);
                        context.finish();

                    } else {
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

    public void makeApiCallResendOtp(LoginModel loginModel, Application application, Activity context) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
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

    public void makeApiCallResendEmailOtp(EmailVerificationModel emailVerificationModel, Application application, Activity context) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<EmailVerificationResponse> call = apiService.sendEmailVerifyData(emailVerificationModel);
        call.enqueue(new Callback<EmailVerificationResponse>() {
            @Override
            public void onResponse(Call<EmailVerificationResponse> call, Response<EmailVerificationResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {

            }
        });

    }
}
