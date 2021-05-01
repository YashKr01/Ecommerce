package com.shopping.bloom.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentlyViewedRepository {

    public LiveData<List<RecentlyViewedItem>> getRecentlyViewedList(
            Application context,
            String pageNo,
            String limit
    ) {

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        MutableLiveData<List<RecentlyViewedItem>> listData = new MutableLiveData<>();

        RetrofitBuilder.getInstance(context).getApi()
                .getRecentlyViewedList(pageNo, limit, "Bearer " + token)
                .enqueue(new Callback<RecentlyViewedResponse>() {

                    @Override
                    public void onResponse(Call<RecentlyViewedResponse> call, Response<RecentlyViewedResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getRecentlyViewedItemList() != null)
                                listData.postValue(response.body().getRecentlyViewedItemList());
                        } else {
                            listData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecentlyViewedResponse> call, Throwable t) {
                        listData.postValue(null);
                    }
                });

        return listData;
    }

}
