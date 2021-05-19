package com.shopping.bloom.model;

import java.util.List;

public class ProductSuggestion {
    public int id;
    public String product_name;
    public String description;
    public String price;
    public String mrp;
    public String primary_image;
    public String avg_rating;
    public String is_on_sale;
    public String sale_price;
    public String sale_percentage;
    public String is_new;
    public Object deleted_at;
    public String created_at;
    public String updated_at;
    public String sellCount;
    public String product_variables_count;
    public List<ColorImagesArray> colorsImageArray;

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

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
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

    public String getSellCount() {
        return sellCount;
    }

    public void setSellCount(String sellCount) {
        this.sellCount = sellCount;
    }

    public String getProduct_variables_count() {
        return product_variables_count;
    }

    public void setProduct_variables_count(String product_variables_count) {
        this.product_variables_count = product_variables_count;
    }

    public List<ColorImagesArray> getColorsImageArray() {
        return colorsImageArray;
    }

    public void setColorsImageArray(List<ColorImagesArray> colorsImageArray) {
        this.colorsImageArray = colorsImageArray;
    }
}
