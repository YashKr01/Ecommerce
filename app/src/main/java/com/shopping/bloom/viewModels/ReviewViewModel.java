package com.shopping.bloom.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shopping.bloom.database.repository.ReviewRepository;
import com.shopping.bloom.model.review.PostReview;
import com.shopping.bloom.model.review.ReviewModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewViewModel extends AndroidViewModel {

    private Application application;
    private ReviewRepository repository;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.repository = new ReviewRepository();
    }

    public LiveData<ReviewModel> getReviews(String productId,
                                            String limit, String page) {
        return repository.getReviews(application, productId, limit, page);
    }

    public void postReview(PostReview postReview) {

        ApiInterface apiInterface = RetrofitBuilder.getInstance(application).getApi();
        apiInterface.postReview(postReview).enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                Log.d("REVIEW", "onResponse: " + response.code());
            }
            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {
                Log.d("REVIEW", "onFailure: " + t.getMessage());
            }
        });

    }
}
