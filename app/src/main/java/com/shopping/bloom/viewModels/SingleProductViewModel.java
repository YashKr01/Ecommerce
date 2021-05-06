package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.App;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.SingleProductResponse;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleProductViewModel extends ViewModel {

    private final MutableLiveData<SingleProductDataResponse> mutableLiveData;
    private final MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData;

    public SingleProductViewModel() {
        mutableLiveData = new MutableLiveData<>();
        loginResponseModelMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponseModel> getLoginResponseModelMutableLiveData(){
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
                mutableLiveData.postValue(response.body().getSingleProductDataResponse());
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

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call = apiService.createUserActivity(product_id, category_id, "Bearer " + token);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                loginResponseModelMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }
}
