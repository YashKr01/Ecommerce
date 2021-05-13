package com.shopping.bloom.viewModels.coupon;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shopping.bloom.database.repository.CouponRepository;
import com.shopping.bloom.model.coupons.Coupon;

import java.util.List;

public class UnusedCouponsViewModel extends AndroidViewModel {

    private CouponRepository repository;
    private Application application;

    public UnusedCouponsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.repository = new CouponRepository();
    }

    public LiveData<List<Coupon>> getCouponsList(){
        return repository.getCouponList(application);
    }

}
