package com.shopping.bloom.database.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.model.review.ReviewModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {

    public LiveData<ReviewModel> getReviews(Application context, String productId,
                                            String limit, String page) {

        MutableLiveData<ReviewModel> liveData = new MutableLiveData<>();

        ApiInterface apiInterface = RetrofitBuilder.getInstance(context).getApi();
        apiInterface.getReviews(productId, limit, page)
                .enqueue(new Callback<ReviewModel>() {
                    @Override
                    public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {

                        Log.d("RETROFIT", "onResponse: CODE " + response.code() + response.message());
                        if (response.isSuccessful()) {
                            liveData.postValue(response.body());
                        } else {
                            liveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewModel> call, Throwable t) {
                        Log.d("RETROFIT", "onFailure: " + t.getMessage());
                        liveData.postValue(null);
                    }
                });

        return liveData;
    }

}