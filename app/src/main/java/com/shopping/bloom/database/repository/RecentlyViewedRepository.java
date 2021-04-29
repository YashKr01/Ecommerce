package com.shopping.bloom.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;

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

        MutableLiveData<List<RecentlyViewedItem>> listData = new MutableLiveData<>();

        RetrofitBuilder.getInstance(context).getApi()
                .getRecentlyViewedList(pageNo, limit)
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
