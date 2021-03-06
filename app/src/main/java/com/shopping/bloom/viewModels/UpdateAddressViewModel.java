package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.App;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAddressViewModel extends ViewModel {
    private final MutableLiveData<LoginResponseModel> mutableLiveData;

    public UpdateAddressViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponseModel> getMutableLiveData() {
        return mutableLiveData;
    }

    public void makeApiCall(String id, AddAddressModel addAddressModel, Application application) {
        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call = apiService.updateAddress(id,addAddressModel.getAddress_name(),
                addAddressModel.getIs_primary(), addAddressModel.getPincode(), addAddressModel.getAddress_line_1(),
                addAddressModel.getCity(), addAddressModel.getContact_number(), "Bearer " + token);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    mutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }
}
