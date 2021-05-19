package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.model.search.SearchProduct;
import com.shopping.bloom.model.search.SearchResponse;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private Application application;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public MutableLiveData<List<SearchProduct>> getSearchProducts(String limit, String page, String query) {

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) token = loginManager.gettoken();
        else token = loginManager.getGuest_token();

        MutableLiveData<List<SearchProduct>> data = new MutableLiveData<>();

        RetrofitBuilder.getInstance(application).getApi()
                .getSearchedProducts("Bearer " + token, limit, page, query)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getSuccess() && response.body().getSearchProducts() != null) {
                                data.postValue(response.body().getSearchProducts());
                            } else data.postValue(null);

                        } else data.postValue(null);

                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        data.postValue(null);
                    }
                });

        return data;
    }


}
