package com.shopping.bloom.restService;

import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.model.PinCodeResponse;
import com.shopping.bloom.model.ProductIds;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.model.coupons.CouponResponse;
import com.shopping.bloom.model.newfragment.NewProductsResponse;
import com.shopping.bloom.model.review.PostReview;
import com.shopping.bloom.model.review.ReviewModel;
import com.shopping.bloom.model.search.SearchResponse;
import com.shopping.bloom.restService.response.AddAddressResponse;
import com.shopping.bloom.restService.response.AddressResponse;
import com.shopping.bloom.restService.response.EmailVerificationResponse;
import com.shopping.bloom.restService.response.GetAvailablePromoResponse;
import com.shopping.bloom.restService.response.GetCartValueResponse;
import com.shopping.bloom.restService.response.GetCategoryResponse;
import com.shopping.bloom.restService.response.GetCheckoutResponse;
import com.shopping.bloom.restService.response.GetColorAndSizeResponse;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.restService.response.GetSingleProductResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.LoginWithPassResponseModel;
import com.shopping.bloom.restService.response.OtpResponseModel;
import com.shopping.bloom.restService.response.PostCheckoutData;
import com.shopping.bloom.restService.response.PostProductList;
import com.shopping.bloom.restService.response.PutWishListRequest;
import com.shopping.bloom.restService.response.RefreshTokenResponse;
import com.shopping.bloom.restService.response.RegisterResponseModel;
import com.shopping.bloom.restService.response.SingleProductResponse;
import com.shopping.bloom.restService.response.SplashBearerResponse;

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

    /*USE THIS CLASS FOR FETCHING THE RANDOM PRODUCTS*/
    @GET("/api/frontend/getRandomProducts")
    @Headers("Accept-type: application/json")
    Call<GetProductsResponse> getRandomProducts(
            @Header("Authorization") String authToken,
            @Query("pageNo") int pageNo,
            @Query("limit") int limit
    );

    @GET("/api/frontend/getRecommendedProducts")
    @Headers("Accept-type: application/json")
    Call<GetProductsResponse> getRecommendedProducts(
            @Header("Authorization") String authToken,
            @Query("pageNo") int pageNo,
            @Query("limit") int limit
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
            @Query("category_id") String categoryId,
            @Query("sub_category_id") String subCategoryId,
            @Query("limit") int limit,
            @Query("pageNo") int pageNo,
            @Query("sortByPrice") String sortByPrice,
            @Query("colors") String colors,
            @Query("sizes") String sizes,
            @Query("bestSelling") String mostPopular,
            @Query("priceRangeLow") String priceLow,
            @Query("priceRangeHigh") String priceHigh,
            @Query("salePercentageRangeLow") String salePercentageLow,
            @Query("salePercentageRangeHigh") String salePercentageHigh
            //TODO: Add one more filter option here New Arrival
    );

    @GET("/api/frontend/getSizeColorData")
    @Headers("Accept-type: application/json")
    Call<GetColorAndSizeResponse> getAvailableFilter(
            @Header("Authorization") String authToken
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
            @Body ProductIds product_ids);

    @GET("frontend/getProductReview")
    Call<ReviewModel> getReviews(@Query("product_id") String productId,
                                 @Query("limit") String limit,
                                 @Query("pageNo") String pageNo,
                                 @Header("Authorization") String bearer);

    @FormUrlEncoded
    @POST("auth/customerRegistrationWithImei")
    Call<SplashBearerResponse> sendBearerToken(@Field("imei_number") String imei_number);

    @POST("metadata/createProductReview")
    Call<ReviewModel> postReview(@Body PostReview review,
                                 @Header("Authorization") String bearer);

    @FormUrlEncoded
    @POST("metadata/createUserAddress")
    Call<AddAddressResponse> postAddress(
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

    @FormUrlEncoded
    @POST("metadata/updateUserAddress/{id}")
    Call<LoginResponseModel> updateAddress(
            @Path("id") String id,
            @Field("address_name") String address_name,
            @Field("is_primary") int is_primary,
            @Field("pincode") String pincode,
            @Field("address_line_1") String address_line_1,
            @Field("city") String city,
            @Field("contact_number") String contact_number,
            @Header("Authorization") String bearer);

    @GET("metadata/getUserWishlist")
    Call<GetProductsResponse> getWishList(
            @Header("Authorization") String bearer,
            @Query("pageNo") int pageNo,
            @Query("limit") int limit);

    @GET("frontend/getNewProducts")
    Call<NewProductsResponse> getNewProducts(@Header("Authorization") String bearer);

    @GET("frontend/getSingleProductInfo/{id}")
    Call<SingleProductResponse> getSingleProduct(
            @Header("Authorization") String bearer,
            @Path("id") int id
    );

    @GET("metadata/recentlyViewProducts")
    Call<GetProductsResponse> getRecentlyViewedList(
            @Header("Authorization") String bearer,
            @Query("pageNo") int pageNo,
            @Query("limit") int limit
    );

    @GET("frontend/checkTokenExpiry")
    Call<RefreshTokenResponse> checkBearerToken(@Header("Authorization") String bearer);

    @GET("/api/frontend/searchProductByName")
    Call<SearchResponse> getSearchedProducts(
            @Header("Authorization") String authToken,
            @Query("limit") String limit,
            @Query("pageNo") String pageNo,
            @Query("searchTerm") String searchQuery
    );

    //for category id
    @FormUrlEncoded
    @POST("metadata/createUserActivity")
    Call<LoginResponseModel> createUserActivity(
            @Field("product_id") String product_id,
            @Field("category_id") int category_id,
            @Header("Authorization") String bearer);

    @POST("order/cart")
    @Headers("Accept-type: application/json")
    Call<GetCartValueResponse> getCartValue(
            @Header("Authorization") String bearer,
            @Body PostProductList productList
    );

    @POST("order/checkout")
    @Headers("Accept-type: application/json")
    Call<GetCheckoutResponse> getCheckoutResponse(
            @Header("Authorization") String bearer,
            @Body PostCheckoutData postCheckoutData);

    @GET("order/getAvailablePromocodes")
    @Headers("Accept-type: application/json")
    Call<GetAvailablePromoResponse> getAvailablePromocode(
            @Header("Authorization") String bearer);


    @GET("metadata/checkDeliveryAvailable")
    Call<PinCodeResponse> checkPinCode(
            @Header("Authorization") String bearer,
            @Query("pincode") String pincode
    );

    @GET("/api/order/getAvailablePromocodes")
    @Headers("Content-type: application/json")
    Call<CouponResponse> getCouponResponse(@Header("Authorization") String authToken);


    /*
    *   Product suggestion below the shopping bag cart items
    * */
    @GET("/api/frontend/getSingleChildProducts")
    @Headers("Accept-type: application/json")
    Call<GetSingleProductResponse> getSingleProductSuggestion(
            @Header("Authorization") String authToken,
            @Query("pageNo") int pageNo,
            @Query("limit") int limit
    );

}
