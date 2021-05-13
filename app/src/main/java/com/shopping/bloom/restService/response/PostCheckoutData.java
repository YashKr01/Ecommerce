package com.shopping.bloom.restService.response;

import com.google.gson.annotations.SerializedName;
import com.shopping.bloom.model.PostCartProduct;

import java.util.List;

public class PostCheckoutData {

    @SerializedName("address_id")
    int addressID;

    @SerializedName("use_wallet_balance")
    int useWalletBalance = 0;

    @SerializedName("promocode")
    String promocode = null;

    @SerializedName("products_list")
    List<PostCartProduct> productList;

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public int getUseWalletBalance() {
        return useWalletBalance;
    }

    public void setUseWalletBalance(int useWalletBalance) {
        this.useWalletBalance = useWalletBalance;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public List<PostCartProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<PostCartProduct> productList) {
        this.productList = productList;
    }
}
