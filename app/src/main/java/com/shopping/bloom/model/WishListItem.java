package com.shopping.bloom.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "wishListItem")
public class WishListItem {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String productId;
    private String userId;

    public WishListItem(String productId, String userId) {
        this.productId = productId;
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "WishListItem{" +
                "productId='" + productId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
