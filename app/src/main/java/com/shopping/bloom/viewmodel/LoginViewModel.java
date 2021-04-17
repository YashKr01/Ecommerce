package com.shopping.bloom.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.shopping.bloom.activities.OtpActivity;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.LoginResponseModel;
import com.shopping.bloom.network.ApiServiceLogin;
import com.shopping.bloom.network.RetroInstance;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    public LoginViewModel() {

    }

    public void makeApiCall(LoginModel loginModel, Context context) {
        ApiServiceLogin apiService = RetroInstance.getRetrofitClient().create(ApiServiceLogin.class);
        Call<LoginResponseModel> call = apiService.sendLoginData(loginModel);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                try {
                    if (response.isSuccessful()) {
                        String success = response.body().getSuccess();
                        String message = response.body().getMessage();

                        if (success.equals("true")) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, OtpActivity.class);
                            intent.putExtra("mobile_no", loginModel.getMobile_no());
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context, message,Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
