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

    @SerializedName("is_on_sale")
    @Expose
    String is_on_sale;

    @SerializedName("sale_price")
    @Expose
    String sale_price;

    @SerializedName("sale_percentage")
    @Expose
    String sale_percentage;

    @SerializedName("price")
    @Expose
    String price;

    public String getIs_on_sale() {
        return is_on_sale;
    }

    public void setIs_on_sale(String is_on_sale) {
        this.is_on_sale = is_on_sale;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getSale_percentage() {
        return sale_percentage;
    }

    public void setSale_percentage(String sale_percentage) {
        this.sale_percentage = sale_percentage;
    }

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

    @Override
    public String toString() {
        return "ProductVariableResponse{" +
                "parentId='" + parentId + '\'' +
                ", childId='" + childId + '\'' +
                ", primary_image='" + primary_image + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
