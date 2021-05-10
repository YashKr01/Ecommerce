package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariableResponse {

    @SerializedName("id")
    String parentId;

    @SerializedName("product_id")
    String childId;

    @SerializedName("primary_image")
    @Expose
    String primary_image;

    @SerializedName("color")
    @Expose
    String color;

    @SerializedName("size")
    @Expose
    String size;

    @SerializedName("price")
    @Expose
    String price;

    @SerializedName("quantity")
    String quantity;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ProductVariableResponse{" +
                "parentId='" + parentId + '\'' +
                ", childId='" + childId + '\'' +
                ", primary_image='" + primary_image + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
