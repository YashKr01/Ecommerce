package com.shopping.bloom.viewModels.newfragment;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.model.newfragment.NewProductCategory;
import com.shopping.bloom.model.newfragment.NewProductsResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFragmentViewModel extends AndroidViewModel {

    private Application application;

    public NewFragmentViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<NewProductCategory> getNewProducts() {
        ApiInterface apiInterface = RetrofitBuilder.getInstance(application).getApi();

        MutableLiveData<NewProductCategory> liveData = new MutableLiveData<>();

        apiInterface.getNewProducts().enqueue(new Callback<NewProductsResponse>() {
            @Override
            public void onResponse(Call<NewProductsResponse> call, Response<NewProductsResponse> response) {
                Log.d("NEW_PRODUCTS", "onResponse: " +
                        response.code() + " " +
                        response.message());
            }

            @Override
            public void onFailure(Call<NewProductsResponse> call, Throwable t) {
                Log.d("NEW_PRODUCTS", "onFailure: " + t.getMessage());
                liveData.postValue(null);
            }
        });

        return liveData;
    }

}
