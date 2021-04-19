package com.shopping.bloom.model;

import java.util.List;

/* Product category with list of product item*/

public class Product {
    private int id;
    private String parent_id;
    private String category_name;
    private String category_thumbnail;
    private String is_bigthumbnail_show;
    private String big_thumbnail;
    private String type;
    private String created_at;
    private String updated_at;
    public Object square_thumbnail;
    private List<SubProduct> sub_category;

    public Product(int id, String parent_id, String category_name, String category_thumbnail,
                   String is_bigthumbnail_show, String big_thumbnail, String type, String created_at,
                   String updated_at, List<SubProduct> sub_category) {
        this.id = id;
        this.parent_id = parent_id;
        this.category_name = category_name;
        this.category_thumbnail = category_thumbnail;
        this.is_bigthumbnail_show = is_bigthumbnail_show;
        this.big_thumbnail = big_thumbnail;
        this.type = type;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.sub_category = sub_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_thumbnail() {
        return category_thumbnail;
    }

    public void setCategory_thumbnail(String category_thumbnail) {
        this.category_thumbnail = category_thumbnail;
    }

    public String getIs_bigthumbnail_show() {
        return is_bigthumbnail_show;
    }

    public void setIs_bigthumbnail_show(String is_bigthumbnail_show) {
        this.is_bigthumbnail_show = is_bigthumbnail_show;
    }

    public String getBig_thumbnail() {
        return big_thumbnail;
    }

    public void setBig_thumbnail(String big_thumbnail) {
        this.big_thumbnail = big_thumbnail;
    }

    public String getType() {
        return type;
    }

    public Object getSquare_thumbnail() {
        return square_thumbnail;
    }

    public void setSquare_thumbnail(Object square_thumbnail) {
        this.square_thumbnail = square_thumbnail;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<SubProduct> getSub_category() {
        return sub_category;
    }

    public void setSub_category(List<SubProduct> sub_category) {
        this.sub_category = sub_category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", parent_id='" + parent_id + '\'' +
                ", category_name='" + category_name + '\'' +
                ", category_thumbnail='" + category_thumbnail + '\'' +
                ", is_bigthumbnail_show='" + is_bigthumbnail_show + '\'' +
                ", big_thumbnail='" + big_thumbnail + '\'' +
                ", type='" + type + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", sub_category=" + sub_category +
                '}';
    }
}
