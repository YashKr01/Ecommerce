package com.shopping.bloom.viewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.shopping.bloom.activities.LoginActivity;
import com.shopping.bloom.activities.OtpActivity;
import com.shopping.bloom.activities.RegisterActivity;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.RegisterResponseModel;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.utils.NetworkCheck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    public RegisterViewModel() {

    }

    public void makeApiCall(RegistrationModel registrationModel, Application application, Activity context){

        if(NetworkCheck.isConnect(context)){

        }

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<RegisterResponseModel> call = apiService.sendRegisterData(registrationModel);
        call.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                System.out.println(response);
                if(response.isSuccessful()){
                    String success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if(success.equals("true")){
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent( context, OtpActivity.class);
                        intent.putExtra("mobile_no",registrationModel.getMobile_no());
                        intent.putExtra("activityName", "RegisterActivity");
                        context.startActivity(intent);
                    }
                    else{
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }

                }
                if(response.code() == 412) {
                    Toast.makeText(context, "Number or Email already been taken", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    context.finish();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                Toast.makeText(context, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });

    }
}
