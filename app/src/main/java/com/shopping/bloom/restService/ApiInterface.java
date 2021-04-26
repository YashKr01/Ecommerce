package com.shopping.bloom.restService;

import com.shopping.bloom.App;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.model.review.PostReview;
import com.shopping.bloom.model.review.Review;
import com.shopping.bloom.model.review.ReviewModel;
import com.shopping.bloom.restService.response.AddressResponse;
import com.shopping.bloom.model.wishlist.WishList;
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
import com.shopping.bloom.restService.response.SplashBearerResponse;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
            @Query("pageNo") int pageNo
    );

    @POST("auth/loginWithEmailPassword")
    @Headers("Content-type: application/json")
    Call<LoginWithPassResponseModel> sendLoginWithEmailPassData(@Body LoginWithEmailPassModel loginWithEmailPassModel);

    @POST("auth/loginWithMobileNoPassword")
    @Headers("Content-type: application/json")
    Call<LoginWithPassResponseModel> sendLoginWithNumberPassData(@Body LoginWithNumberPassModel loginWithNumberPassModel);

    @POST("metadata/addUserWishList")
    @Headers("Content-type: application/json")
    Call<PutWishListRequest> postUserWishList(
            @Header("Authorization") String authToken,
            @Body List<String> product_ids);

    @GET("frontend/getProductReview")
    //@Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9ibG9vbWFwcC5pblwvYXBpXC9hdXRoXC9jdXN0b21lclJlZ2lzdHJhdGlvbldpdGhJbWVpIiwiaWF0IjoxNjE5MjcyMDgzLCJleHAiOjE2MjE4NjQwODMsIm5iZiI6MTYxOTI3MjA4MywianRpIjoidnVLVzhMUjN4NjNhUGtXdCIsInN1YiI6NDAsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.oAnRkVe-HxNSVYTr6fqQJkTbIJj6KwVmfo9mjeD3IvE")
    Call<ReviewModel> getReviews(@Query("product_id") String productId,
                                 @Query("limit") String limit,
                                 @Query("pageNo") String pageNo,
                                 @Header("Authorization") String bearer);


    @FormUrlEncoded
    @POST("auth/customerRegistrationWithImei")
    Call<SplashBearerResponse> sendBearerToken(@Field("imei_number") String imei_number);

    @POST("metadata/createProductReview")
    //@Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9ibG9vbWFwcC5pblwvYXBpXC9hdXRoXC9jdXN0b21lclJlZ2lzdHJhdGlvbldpdGhJbWVpIiwiaWF0IjoxNjE5MjcyMDgzLCJleHAiOjE2MjE4NjQwODMsIm5iZiI6MTYxOTI3MjA4MywianRpIjoidnVLVzhMUjN4NjNhUGtXdCIsInN1YiI6NDAsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.oAnRkVe-HxNSVYTr6fqQJkTbIJj6KwVmfo9mjeD3IvE")
    Call<ReviewModel> postReview(@Body PostReview review,
                                 @Header("Authorization") String bearer);

    @FormUrlEncoded
    @POST("metadata/createUserAddress")
    Call<LoginResponseModel> postAddress(
            @Field("address_name") String address_name,
            @Field("is_primary") int is_primary,
            @Field("pincode") String pincode,
            @Field("address_line_1") String address_line_1,
            @Field("city") String city,
            @Field("contact_number") String contact_number,
            @Header("Authorization") String bearer
    );

    @GET("metadata/getUserAddress")
    Call<AddressResponse> getAddress(
            @Query("pageNo") int pageNo,
            @Query("limit") int limit,
            @Header("Authorization") String bearer
    );

    @DELETE("metadata/deleteUserAddress/{id}")
    Call<LoginResponseModel> deleteAddress(@Path("id") String id,
                                           @Header("Authorization") String bearer);

    @GET("metadata/getUserWishlist")
    @Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9ibG9vbWFwcC5pblwvYXBpXC9hdXRoXC9jdXN0b21lclJlZ2lzdHJhdGlvbldpdGhJbWVpIiwiaWF0IjoxNjE5NDMxMTUzLCJleHAiOjE2MjIwMjMxNTMsIm5iZiI6MTYxOTQzMTE1MywianRpIjoiblI5Zlc2dTJLUjJ5cGpFNCIsInN1YiI6MzgsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.16WTQmSWiGRSKvxf-DvnDzl73nq9MWiW85I6Wu_hhYM")
    Call<WishList> getWishList(@Query("pageNo") String pageNo, @Query("limit") String limit);

}
