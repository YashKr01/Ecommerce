package com.shopping.bloom.viewModels.wishlist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.model.wishlist.WishList;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.utils.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListViewModel extends AndroidViewModel {

    private Application application;

    public WishListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public MutableLiveData<WishList> getWishList(String pageNo, String limit) {
        ApiInterface apiInterface = RetrofitBuilder.getInstance(application).getApi();
        MutableLiveData<WishList> data = new MutableLiveData<>();

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        apiInterface.getWishList(pageNo, limit, "Bearer " + token).enqueue(new Callback<WishList>() {
            @Override
            public void onResponse(Call<WishList> call, Response<WishList> response) {
                Log.d("WISHLIST", "onResponse: " + response.code() + " " + response.message());
                Log.d("WISHLIST", "onResponse: " + response.body().getMessage());

                if (response.isSuccessful()) {
                    data.postValue(response.body());
                } else {
                    data.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<WishList> call, Throwable t) {
                Log.d("WISHLIST", "onFailure: " + t.getMessage());
                data.postValue(null);
            }
        });

        return data;
    }

}
