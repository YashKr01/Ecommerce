package com.shopping.bloom.network;

import com.shopping.bloom.model.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServiceLogin {
    @Headers({
            "Content-Type:application/json"
    })

    @POST("auth/customerLoginWithOtp")
    Call<LoginResponseModel> sendLoginData(@Body String mobile_no);
}
