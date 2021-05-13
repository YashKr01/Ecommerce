package com.shopping.bloom.model.coupons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponResponse {

    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("data")
    private List<Coupon> couponList;

    public boolean isSuccess() {
        return success;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }
}
