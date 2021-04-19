package com.shopping.bloom.restService;

import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.restService.response.EmailVerificationResponse;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.restService.response.GetCategoryResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.restService.response.RegisterResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/api/frontend/getCategory")
    @Headers("Accept-type: application/json")
    Call<GetCategoryResponse> getCategory(
            @Query("mainCategory") String mainCategory,
            @Query("limit") int limit,
            @Query("pageNo") int pageNo,
            @Query("category_name") String category_name
    );

    @POST("auth/customerRegistration")
    @Headers("Content-type: application/json")
    Call<RegisterResponseModel> sendRegisterData(@Body RegistrationModel registrationModel);

    @POST("auth/verifyMobileOtp")
    @Headers("Content-type: application/json")
    Call<OtpResponseModel> sendOtp(@Body OtpModel otpModel);

    @POST("auth/resendOtp")
    @Headers("Content-type: application/json")
    Call<LoginResponseModel> resendOtp(@Body LoginModel loginModel);

    @POST("auth/customerLoginWithOtp")
    @Headers("Content-type: application/json")
    Call<LoginResponseModel> sendLoginData(@Body LoginModel loginModel);

    @POST("auth/resendEmailOtp")
    @Headers("Content-type: application/json")
    Call<EmailVerificationResponse> sendEmailVerifyData(@Body EmailVerificationModel emailOtpModel);

    @POST("auth/verifyEmailOtp")
    @Headers("Content-type: application/json")
    Call<OtpResponseModel> verifyEmailOtp(@Body EmailOtpModel emailOtpModel);
}
