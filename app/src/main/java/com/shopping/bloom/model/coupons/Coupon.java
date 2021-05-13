package com.shopping.bloom.model.coupons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {

    @Expose
    @SerializedName("id")
    private Integer id;
    @Expose
    @SerializedName("promocode")
    private String promoCode;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("minimal_cart_total")
    private String minimumAmount;
    @Expose
    @SerializedName("start_from")
    private String date;
    @Expose
    @SerializedName("is_active")
    private String isActive;
    @Expose
    @SerializedName("max_discount")
    private Double discount;

    public Integer getId() {
        return id;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public String getType() {
        return type;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public String getDate() {
        return date;
    }

    public String getIsActive() {
        return isActive;
    }

    public Double getDiscount() {
        return discount;
    }
}
