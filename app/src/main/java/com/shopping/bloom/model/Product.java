package com.shopping.bloom.model;

import java.util.List;

public class Product {
    private int id;
    private String product_name;
    private String description;
    private String price;
    private String mrp;
    private String available_sizes;
    private String available_colors;
    private String primary_image;
    private String avg_rating;
    private String is_on_sale;
    private String sale_price;
    private String sale_percentage;
    private String is_new;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private boolean isInUserWishList;
    private List<ColorImageArray> colorsImageArray;

    public Product(int id, String product_name, String description, String price, String mrp,
                   String available_sizes, String available_colors, String primary_image,
                   String avg_rating, String is_on_sale, String sale_price, String sale_percentage,
                   String is_new, String deleted_at, String created_at, String updated_at, boolean isInUserWishList) {
        this.id = id;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.mrp = mrp;
        this.available_sizes = available_sizes;
        this.available_colors = available_colors;
        this.primary_image = primary_image;
        this.avg_rating = avg_rating;
        this.is_on_sale = is_on_sale;
        this.sale_price = sale_price;
        this.sale_percentage = sale_percentage;
        this.is_new = is_new;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.isInUserWishList = isInUserWishList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
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

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

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

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public List<ColorImageArray> getColorsImageArray() {
        return colorsImageArray;
    }

    public void setColorsImageArray(List<ColorImageArray> colorsImageArray) {
        this.colorsImageArray = colorsImageArray;
    }

    public boolean isInUserWishList() {
        return isInUserWishList;
    }

    public void setInUserWishList(boolean inUserWishList) {
        isInUserWishList = inUserWishList;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", mrp='" + mrp + '\'' +
                ", available_sizes='" + available_sizes + '\'' +
                ", available_colors='" + available_colors + '\'' +
                ", primary_image='" + primary_image + '\'' +
                ", avg_rating='" + avg_rating + '\'' +
                ", is_on_sale='" + is_on_sale + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", sale_percentage='" + sale_percentage + '\'' +
                ", is_new='" + is_new + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", isInUserWishList=" + isInUserWishList +
                ", colorsImageArray=" + colorsImageArray +
                '}';
    }
}
