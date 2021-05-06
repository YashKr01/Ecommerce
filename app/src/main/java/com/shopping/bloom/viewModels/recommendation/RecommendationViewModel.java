package com.shopping.bloom.viewModels.recommendation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.model.wishlist.recommendations.RecommendationItem;
import com.shopping.bloom.model.wishlist.recommendations.RecommendationResponse;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationViewModel extends AndroidViewModel {

    private Application application;

    public RecommendationViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<RecommendationItem>> getRecommendationList(String limit, String page) {

        MutableLiveData<List<RecommendationItem>> data = new MutableLiveData<>();
        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) token = loginManager.gettoken();
        else token = loginManager.getGuest_token();

        RetrofitBuilder.getInstance(application).getApi()
                .getRecommendationResponse("Bearer " + token, limit, page)
                .enqueue(new Callback<RecommendationResponse>() {
                    @Override
                    public void onResponse(Call<RecommendationResponse> call, Response<RecommendationResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess()) {
                                if (response.body().getRecommendationsItemList() != null) {
                                    data.postValue(response.body().getRecommendationsItemList());
                                }
                            } else {
                                data.postValue(null);
                            }
                        } else {
                            data.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecommendationResponse> call, Throwable t) {
                        data.postValue(null);
                    }
                });

        return data;
    }

}
