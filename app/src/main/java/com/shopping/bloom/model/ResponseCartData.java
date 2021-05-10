package com.shopping.bloom.model;

import com.google.gson.annotations.SerializedName;

public class ResponseCartData {
    @SerializedName("subtotal")
    int subTotal;

    public ResponseCartData(int subTotal) {
        this.subTotal = subTotal;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }
}
