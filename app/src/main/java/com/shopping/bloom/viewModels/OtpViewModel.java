package com.shopping.bloom.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
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

    private final MutableLiveData<OtpResponseModel> mutableLiveData, emailModelMutableLiveData;
    private final MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData;
    private final MutableLiveData<EmailVerificationResponse> emailResponseMutableLiveData;

    public OtpViewModel() {
        mutableLiveData = new MutableLiveData<>();
        emailModelMutableLiveData = new MutableLiveData<>();
        loginResponseModelMutableLiveData = new MutableLiveData<>();
        emailResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<OtpResponseModel> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<OtpResponseModel> getEmailModelMutableLiveData(){
        return emailModelMutableLiveData;
    }

    public MutableLiveData<LoginResponseModel> getLoginResponseModelMutableLiveData(){
        return loginResponseModelMutableLiveData;
    }
    public MutableLiveData<EmailVerificationResponse> getEmailResponseMutableLiveData(){
        return emailResponseMutableLiveData;
    }

    public void makeApiCallVerifyOtp(OtpModel otpModel, Application application, Activity context) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<OtpResponseModel> call = apiService.sendOtp(otpModel);
        call.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    mutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<OtpResponseModel> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });

    }

    public void makeApiCallVerifyEmailOtp(EmailOtpModel otpModel, Application application, Activity context) {

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<OtpResponseModel> call = apiService.verifyEmailOtp(otpModel);
        call.enqueue(new Callback<OtpResponseModel>() {
            @Override
            public void onResponse(Call<OtpResponseModel> call, Response<OtpResponseModel> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    emailModelMutableLiveData.postValue(response.body());

                }
            }

            @Override
            public void onFailure(Call<OtpResponseModel> call, Throwable t) {
                emailModelMutableLiveData.postValue(null);
            }
        });

    }

    public void makeApiCallResendOtp(LoginModel loginModel, Application application) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call = apiService.resendOtp(loginModel);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful()) {
                    loginResponseModelMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                loginResponseModelMutableLiveData.postValue(null);
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
                    emailResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                emailResponseMutableLiveData.postValue(null);
            }
        });

    }
}
