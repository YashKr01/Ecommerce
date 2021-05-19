package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleProductDataResponse {

    @SerializedName("available_colors")
    @Expose
    String available_colors;

    @SerializedName("available_sizes")
    @Expose
    String available_sizes;

    @SerializedName("product_name")
    @Expose
    String product_name;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("avg_rating")
    @Expose
    String avg_rating;

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

    @SerializedName("primary_image")
    @Expose
    String primary_image;

    @SerializedName("product_variables")
    @Expose
    List<ProductVariableResponse> productVariableResponses;

    @SerializedName("isInUserWishList")
    boolean isInWishList;

    @SerializedName("product_descriptions")
    @Expose
    List<SingleProductDescResponse> singleProductDescResponseList;

    @SerializedName("product_images")
    @Expose
    List<SingleProductImageResponse> singleProductImageResponses;

    public List<SingleProductImageResponse> getSingleProductImageResponses() {
        return singleProductImageResponses;
    }

    public void setSingleProductImageResponses(List<SingleProductImageResponse> singleProductImageResponses) {
        this.singleProductImageResponses = singleProductImageResponses;
    }

    @SerializedName("id")
    @Expose
    Integer id;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public List<SingleProductDescResponse> getSingleProductDescResponseList() {
        return singleProductDescResponseList;
    }

    public void setSingleProductDescResponseList(List<SingleProductDescResponse> singleProductDescResponseList) {
        this.singleProductDescResponseList = singleProductDescResponseList;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getAvailable_sizes() {
        return available_sizes;
    }

    public void setAvailable_sizes(String available_sizes) {
        this.available_sizes = available_sizes;
    }

    public String getAvailable_colors() {
        return available_colors;
    }

    public void setAvailable_colors(String available_colors) {
        this.available_colors = available_colors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public List<ProductVariableResponse> getProductVariableResponses() {
        return productVariableResponses;
    }

    public void setProductVariableResponses(List<ProductVariableResponse> productVariableResponses) {
        this.productVariableResponses = productVariableResponses;
    }

    public boolean isInWishList() {
        return isInWishList;
    }

    public void setInWishList(boolean inWishList) {
        isInWishList = inWishList;
    }
}
