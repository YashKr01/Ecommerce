package com.shopping.bloom.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.OtpActivity;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.LoginResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public LoginViewModel() {

    }

    public void makeApiCall(LoginModel loginModel, Application application, Activity context) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call = apiService.sendLoginData(loginModel);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                System.out.println(response);
                if (response.isSuccessful()) {
                    String success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success.equals("true")) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, OtpActivity.class);
                        intent.putExtra("mobile_no", loginModel.getMobile_no());
                        intent.putExtra("activityName", "LoginActivity");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                System.out.println(call.request());
                System.out.println(t.toString());
            }
        });

    }
}
