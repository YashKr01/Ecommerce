package com.shopping.bloom.restService;

import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.model.review.PostReview;
import com.shopping.bloom.model.review.Review;
import com.shopping.bloom.model.review.ReviewModel;
import com.shopping.bloom.restService.response.EmailVerificationResponse;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.restService.response.GetCategoryResponse;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.LoginWithPassResponseModel;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.restService.response.RegisterResponseModel;
import com.shopping.bloom.restService.response.SplashBearerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @GET("/api/frontend/getProducts")
    @Headers("Accept-type: application/json")
    Call<GetProductsResponse> getProducts(
            @Query("sub_category_id") String subCategoryId,
            @Query("limit") int limit
    );

    @POST("auth/loginWithEmailPassword")
    @Headers("Content-type: application/json")
    Call<LoginWithPassResponseModel> sendLoginWithEmailPassData(@Body LoginWithEmailPassModel loginWithEmailPassModel);

    @POST("auth/loginWithMobileNoPassword")
    @Headers("Content-type: application/json")
    Call<LoginWithPassResponseModel> sendLoginWithNumberPassData(@Body LoginWithNumberPassModel loginWithNumberPassModel);

    @GET("frontend/getProductReview")
    //@Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC9hdXRoXC92ZXJpZnlNb2JpbGVPdHAiLCJpYXQiOjE2MTg4NDg2OTgsImV4cCI6MTYxODg1MjI5OCwibmJmIjoxNjE4ODQ4Njk4LCJqdGkiOiJnN3lqWXNsWVFOWXFBcGFOIiwic3ViIjoxLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.93CP04ZPpwrzKiKzYa8vcm5ITZrpyaaTsbTSa4ABWNY")
    @Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9ibG9vbWFwcC5pblwvYXBpXC9hdXRoXC9jdXN0b21lclJlZ2lzdHJhdGlvbldpdGhJbWVpIiwiaWF0IjoxNjE5MjcwOTM1LCJleHAiOjE2MjE4NjI5MzUsIm5iZiI6MTYxOTI3MDkzNSwianRpIjoic3FXZ25yV3ZaY2Zzb05NMSIsInN1YiI6MzYsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.JtbSw1DxrmjQ5tnNzGZX-X_hMKYEWpRb4qOR3AWNqGc")
    Call<ReviewModel> getReviews(@Query("product_id") String productId, @Query("limit") String limit, @Query("pageNo") String pageNo);


    @FormUrlEncoded
    @POST("auth/customerRegistrationWithImei")
    Call<SplashBearerResponse> sendBearerToken(@Field("imei_number") String imei_number);

    @POST("metadata/createProductReview")
    @Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC9hdXRoXC92ZXJpZnlNb2JpbGVPdHAiLCJpYXQiOjE2MTg4NDg2OTgsImV4cCI6MTYxODg1MjI5OCwibmJmIjoxNjE4ODQ4Njk4LCJqdGkiOiJnN3lqWXNsWVFOWXFBcGFOIiwic3ViIjoxLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.93CP04ZPpwrzKiKzYa8vcm5ITZrpyaaTsbTSa4ABWNY")
    Call<ReviewModel> postReview(@Body PostReview review);

}
