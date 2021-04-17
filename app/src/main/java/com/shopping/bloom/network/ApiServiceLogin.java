package com.shopping.bloom.network;

import com.google.gson.JsonObject;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.LoginResponseModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServiceLogin {

    @Headers("Content-Type: application/json")

    @POST("auth/customerLoginWithOtp")
    Call<LoginResponseModel> sendLoginData(@Body LoginModel loginModel);
}
