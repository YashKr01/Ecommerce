package com.shopping.bloom.restService;

import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.restService.response.EmailVerificationResponse;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.restService.response.GetCategoryResponse;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.LoginWithPassResponseModel;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.restService.response.PutWishListRequest;
import com.shopping.bloom.restService.response.RegisterResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/api/frontend/getCategory")
    @Headers("Accept-type: application/json")
    Call<GetCategoryResponse> getCategory(
            @Header("Authorization") String authToken,
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
            @Header("Authorization") String authToken,
            @Query("sub_category_id") String subCategoryId,
            @Query("limit") int limit,
            @Query("pageNo") int  pageNo
    );

    @POST("auth/loginWithEmailPassword")
    @Headers("Content-type: application/json")
    Call<LoginWithPassResponseModel> sendLoginWithEmailPassData(@Body LoginWithEmailPassModel loginWithEmailPassModel);

    @POST("auth/loginWithMobileNoPassword")
    @Headers("Content-type: application/json")
    Call<LoginWithPassResponseModel> sendLoginWithNumberPassData(@Body LoginWithNumberPassModel loginWithNumberPassModel);

    @POST("api/metadata/addUserWishList")
    @Headers("Content-type: application/json")
    Call<PutWishListRequest> postUserWishList(
            @Header("Authorization") String authToken,
            @Body List<String> product_ids);
}
