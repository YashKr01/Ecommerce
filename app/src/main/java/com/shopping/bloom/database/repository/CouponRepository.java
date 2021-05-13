package com.shopping.bloom.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.model.coupons.Coupon;
import com.shopping.bloom.model.coupons.CouponResponse;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponRepository {

    public LiveData<List<Coupon>> getCouponList(Application application) {

        //region ::for getting auth token
        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) token = loginManager.gettoken();
        else token = loginManager.getGuest_token();
        // endregion

        MutableLiveData<List<Coupon>> data = new MutableLiveData<>();

        RetrofitBuilder.getInstance(application).getApi().getCouponResponse("Bearer " + token)
                .enqueue(new Callback<CouponResponse>() {
                    @Override
                    public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getCouponList() != null) {
                                data.postValue(response.body().getCouponList());

                            } else data.postValue(null);

                        } else data.postValue(null);

                    }

                    @Override
                    public void onFailure(Call<CouponResponse> call, Throwable t) {
                        data.postValue(null);
                    }
                });

        return data;
    }


}
