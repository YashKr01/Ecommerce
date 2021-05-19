package com.shopping.bloom.viewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.database.repository.ReviewRepository;
import com.shopping.bloom.model.review.PostReview;
import com.shopping.bloom.model.review.ReviewModel;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.utils.LoginManager;

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

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) token = loginManager.gettoken();
        else token = loginManager.getGuest_token();


        RetrofitBuilder.getInstance(application).getApi()
                .postReview(postReview, "Bearer " + token).enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {

                if (response.isSuccessful() && response.body() != null)
                    Toast.makeText(application, "Review Posted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(application, "Couldn't post your review", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {
            }
        });

    }
}
