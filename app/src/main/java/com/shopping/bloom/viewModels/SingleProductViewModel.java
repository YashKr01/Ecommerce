package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.App;
import com.shopping.bloom.R;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.model.RandomImageDataResponse;
import com.shopping.bloom.model.shoppingbag.ProductEntity;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.RandomImageResponse;
import com.shopping.bloom.restService.response.SingleProductResponse;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleProductViewModel extends ViewModel {

    private final MutableLiveData<SingleProductDataResponse> mutableLiveData;
    private final MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData;
    private final MutableLiveData<List<RandomImageDataResponse>> randomImageDataResponseMutableLiveData;

    public SingleProductViewModel() {
        mutableLiveData = new MutableLiveData<>();
        loginResponseModelMutableLiveData = new MutableLiveData<>();
        randomImageDataResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<RandomImageDataResponse>> getRandomImageDataResponseMutableLiveData() {
        return randomImageDataResponseMutableLiveData;
    }

    public MutableLiveData<LoginResponseModel> getLoginResponseModelMutableLiveData() {
        return loginResponseModelMutableLiveData;
    }

    public MutableLiveData<SingleProductDataResponse> getMutableLiveData() {
        return mutableLiveData;
    }

    public void makeApiCall(int id, Application application) {

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<SingleProductResponse> call = apiService.getSingleProduct("Bearer " + token, id);
        call.enqueue(new Callback<SingleProductResponse>() {
            @Override
            public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                if (response.body() == null) {
                    System.out.println("NO DATA");
                } else {
                    mutableLiveData.postValue(response.body().getSingleProductDataResponse());
                }
            }

            @Override
            public void onFailure(Call<SingleProductResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public void makeApiCallCreateUserActivity(String product_id, String category_id, Application application) {

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        //todo here category id is optional if category id value is null then there will be no category id parameter
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call;
        if (category_id == null) {
            call = apiService.createUserActivity(product_id, "Bearer " + token);
        } else {
            call = apiService.createUserActivity(product_id, category_id, "Bearer " + token);
        }
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.body() == null) {
                    System.out.println("NO DATA");
                } else {
                    loginResponseModelMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                loginResponseModelMutableLiveData.postValue(null);
            }
        });
    }

    public void makeApiCallRandomImage(int limit, int pageNo, Application application) {

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<RandomImageResponse> call = apiService.getRandomImage(limit, pageNo, "Bearer " + token);
        call.enqueue(new Callback<RandomImageResponse>() {
            @Override
            public void onResponse(Call<RandomImageResponse> call, Response<RandomImageResponse> response) {
                if (response.body() == null) {
                    System.out.println("NO DATA");
                }else{
                    randomImageDataResponseMutableLiveData.postValue(response.body().getImageDataResponseList());
                }

            }

            @Override
            public void onFailure(Call<RandomImageResponse> call, Throwable t) {
                randomImageDataResponseMutableLiveData.postValue(null);
            }
        });
    }

    public void addToShoppingBag(ProductEntity productEntity) {

        EcommerceDatabase.databaseWriteExecutor.execute(() ->
                EcommerceDatabase.getInstance().wishListProductDao().addToShoppingBag(productEntity)
        );

    }

}
