package com.shopping.bloom.viewModels.newfragment;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.model.newfragment.NewProductCategory;
import com.shopping.bloom.model.newfragment.NewProductsResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFragmentViewModel extends AndroidViewModel {

    private Application application;

    public NewFragmentViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<NewProductCategory>> getNewProducts() {

        MutableLiveData<List<NewProductCategory>> liveData = new MutableLiveData<>();

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) token = loginManager.gettoken();
        else token = loginManager.getGuest_token();

        RetrofitBuilder.getInstance(application).getApi()
                .getNewProducts("Bearer " + token).enqueue(new Callback<NewProductsResponse>() {

            @Override
            public void onResponse(Call<NewProductsResponse> call, Response<NewProductsResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().getNewCategoryList() != null)
                        liveData.postValue(response.body().getNewCategoryList());

                    else liveData.postValue(null);

                } else liveData.postValue(null);

            }

            @Override
            public void onFailure(Call<NewProductsResponse> call, Throwable t) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }

}
