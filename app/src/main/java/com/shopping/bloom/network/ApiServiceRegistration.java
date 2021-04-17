package com.shopping.bloom.network;

import com.shopping.bloom.restService.response.RegisterResponseModel;
import com.shopping.bloom.model.RegistrationModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServiceRegistration {
    @Headers({
            "Content-Type:application/json"
    })

    @POST("api/auth/customerRegistration")
    Call<RegisterResponseModel> sendRegisterData(@Body RegistrationModel registrationModel);
}