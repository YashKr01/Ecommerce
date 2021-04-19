package com.shopping.bloom.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.OtpActivity;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.network.ApiServiceLogin;
import com.shopping.bloom.network.RetroInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public LoginViewModel() {

    }

    public void makeApiCall(LoginModel loginModel, Activity context) {
        ApiServiceLogin apiService = RetroInstance.getRetrofitClient().create(ApiServiceLogin.class);
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
                        context.startActivity(intent);
                        context.finish();
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
