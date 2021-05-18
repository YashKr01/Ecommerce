package com.shopping.bloom.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.LoginWithPassActivity;
import com.shopping.bloom.activities.MainActivity;
import com.shopping.bloom.activities.OtpActivity;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.LoginWithPassResponseModel;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginWithPassViewModel extends ViewModel {

    LoginManager loginManager;

    private final MutableLiveData<LoginWithPassResponseModel> mobileMutableData, emailMutableData;

    public LoginWithPassViewModel() {
        mobileMutableData = new MutableLiveData<>();
        emailMutableData = new MutableLiveData<>();
    }

    public MutableLiveData<LoginWithPassResponseModel> getMobileMutableLiveData() {
        return mobileMutableData;
    }

    public MutableLiveData<LoginWithPassResponseModel> getEmailModelMutableLiveData(){
        return emailMutableData;
    }

    public void makeApiCallWithNumber(LoginWithNumberPassModel loginModel, Application application, Activity context) {
        loginManager = new LoginManager(context);
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginWithPassResponseModel> call = apiService.sendLoginWithNumberPassData(loginModel);
        call.enqueue(new Callback<LoginWithPassResponseModel>() {
            @Override
            public void onResponse(Call<LoginWithPassResponseModel> call, Response<LoginWithPassResponseModel> response) {
//                System.out.println(response.body().getData().getUserInfo().getEmail());
                if (response.isSuccessful() && response.body() != null) {
                    mobileMutableData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginWithPassResponseModel> call, Throwable t) {
                mobileMutableData.postValue(null);
                System.out.println(call.request());
                System.out.println(t.toString());
            }
        });

    }

    public void makeApiCallWithEmail(LoginWithEmailPassModel loginModel, Application application, Activity context) {
        loginManager = new LoginManager(context);
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginWithPassResponseModel> call = apiService.sendLoginWithEmailPassData(loginModel);
        call.enqueue(new Callback<LoginWithPassResponseModel>() {
            @Override
            public void onResponse(Call<LoginWithPassResponseModel> call, Response<LoginWithPassResponseModel> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    emailMutableData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginWithPassResponseModel> call, Throwable t) {
                System.out.println(call.request());
                System.out.println(t.toString());
            }
        });
    }

}
