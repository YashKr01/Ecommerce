package com.shopping.bloom.model.newfragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewProductCategory {

    @Expose
    @SerializedName("id")
    private Integer id;
    @Expose
    @SerializedName("category_name")
    private String categoryName;
    @Expose
    @SerializedName("big_thumbnail")
    private String thumbNail;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("products")
    private List<NewProduct> newProductList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public List<NewProduct> getNewProductList() {
        return newProductList;
    }

    public void setNewProductList(List<NewProduct> newProductList) {
        this.newProductList = newProductList;
    }
}
