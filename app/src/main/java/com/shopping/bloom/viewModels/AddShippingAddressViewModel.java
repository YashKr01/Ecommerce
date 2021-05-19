package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.App;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.AddAddressResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShippingAddressViewModel extends ViewModel {
    private final MutableLiveData<AddAddressResponse> mutableLiveData;

    public AddShippingAddressViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<AddAddressResponse> getMutableLiveData() {
        return mutableLiveData;
    }

    public void makeApiCall(AddAddressModel addAddressModel, Application application) {
        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<AddAddressResponse> call = apiService.postAddress(addAddressModel.getAddress_name(),
                addAddressModel.getIs_primary(), addAddressModel.getPincode(), addAddressModel.getAddress_line_1(),
                addAddressModel.getCity(), addAddressModel.getContact_number(), "Bearer " + token);
        call.enqueue(new Callback<AddAddressResponse>() {
            @Override
            public void onResponse(Call<AddAddressResponse> call, Response<AddAddressResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    mutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AddAddressResponse> call, Throwable t) {
                System.out.println(call.request());
                System.out.println(t.toString());
            }
        });
    }
}
