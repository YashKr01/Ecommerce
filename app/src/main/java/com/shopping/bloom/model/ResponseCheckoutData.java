package com.shopping.bloom.model;

import com.google.gson.annotations.SerializedName;

public class ResponseCheckoutData {

    @SerializedName("walletBalance")
    int walletBalance;

    @SerializedName("walletBalanceUsed")
    int walletBalanceUsed;

    @SerializedName("shippingCharges")
    int shippingCharges;

    @SerializedName("discountAmount")
    int discountAmount;

    @SerializedName("subtotal")
    int subTotal;

    @SerializedName("total")
    int total;

    public ResponseCheckoutData(int walletBalance, int walletBalanceUsed, int shippingCharges, int discountAmount, int subTotal, int total) {
        this.walletBalance = walletBalance;
        this.walletBalanceUsed = walletBalanceUsed;
        this.shippingCharges = shippingCharges;
        this.discountAmount = discountAmount;
        this.subTotal = subTotal;
        this.total = total;
    }

    public int getWalletBalance() {
        return walletBalance;
    }

    public int getWalletBalanceUsed() {
        return walletBalanceUsed;
    }

    public int getShippingCharges() {
        return shippingCharges;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public int getTotal() {
        return total;
    }
}
