package com.shopping.bloom.viewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

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

    public LoginWithPassViewModel() {

    }

    public void makeApiCallWithNumber(LoginWithNumberPassModel loginModel, Application application, Activity context) {
        loginManager = new LoginManager(context);
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginWithPassResponseModel> call = apiService.sendLoginWithNumberPassData(loginModel);
        call.enqueue(new Callback<LoginWithPassResponseModel>() {
            @Override
            public void onResponse(Call<LoginWithPassResponseModel> call, Response<LoginWithPassResponseModel> response) {
                System.out.println(response.body().getData().getUserInfo().getEmail());
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
                        loginManager.SetLoginStatus(false);
                        String email_verified_at = response.body().getData().getUserInfo().getEmail_verified_at();
                        if(email_verified_at != null || !email_verified_at.isEmpty()|| !email_verified_at.equals("")){
                            loginManager.setEmail_verified_at(true);
                        }

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginWithPassResponseModel> call, Throwable t) {
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
                    String success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success.equals("true")) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

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

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
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
