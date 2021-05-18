package com.shopping.bloom.viewModels;

import android.app.Application;
import android.widget.Toast;

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
    private final MutableLiveData<LoginResponseModel> updateLiveData;
    private final MutableLiveData<LoginResponseModel> deleteLiveData;
    LoginManager loginManager;
    String token;

    public MyAddressViewModel() {
        mutableLiveData = new MutableLiveData<>();
        updateLiveData = new MutableLiveData<>();
        deleteLiveData = new MutableLiveData<>();
        loginManager = new LoginManager(App.getContext());

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }
    }

    public MutableLiveData<List<AddressDataResponse>> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<LoginResponseModel> getUpdateLiveData() {
        return updateLiveData;
    }

    public MutableLiveData<LoginResponseModel> getDeleteLiveData() {
        return deleteLiveData;
    }

    public void makeApiCall(int pageNo, int limit, Application application) {

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<AddressResponse> call = apiService.getAddress(pageNo, limit, "Bearer " + token);
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    mutableLiveData.postValue(response.body().getAddressResponseList());
                else
                    mutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public void updateAddressApiCall(String id, AddAddressModel addAddressModel, Application application) {

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call = apiService.updateAddress(id, addAddressModel.getAddress_name(),
                addAddressModel.getIs_primary(), addAddressModel.getPincode(), addAddressModel.getAddress_line_1(),
                addAddressModel.getCity(), addAddressModel.getContact_number(), "Bearer " + token);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                updateLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                System.out.println(call.request());
                System.out.println(t.toString());
            }
        });
    }

    public void delete(AddressDataResponse address, Application application) {
        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call = apiService.deleteAddress(address.getId(), "Bearer " + token);
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(application.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    deleteLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                deleteLiveData.postValue(null);
            }
        });
    }
}
