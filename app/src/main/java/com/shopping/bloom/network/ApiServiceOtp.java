package com.shopping.bloom.network;


import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.model.OtpResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServiceOtp {
    @Headers({
            "Content-Type:application/json"
    })

    @POST("auth/customerLoginWithOtp")
    Call<OtpResponseModel> sendOtp(@Body OtpModel otpModel);

    @POST("auth/resendOtp")
    Call<OtpResponseModel> resendOtp(@Body String number);
}
