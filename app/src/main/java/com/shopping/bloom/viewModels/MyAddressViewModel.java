package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.App;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.model.AddressDataResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.AddressResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAddressViewModel extends ViewModel {
    private final MutableLiveData<List<AddressDataResponse>> mutableLiveData;

    public MyAddressViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<AddressDataResponse>> getMutableLiveData() {
        return mutableLiveData;
    }

    public void makeApiCall(int pageNo, int limit, Application application) {

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<AddressResponse> call = apiService.getAddress(pageNo, limit, "Bearer " + token);
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                mutableLiveData.postValue(response.body().getAddressResponseList());
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }
}
