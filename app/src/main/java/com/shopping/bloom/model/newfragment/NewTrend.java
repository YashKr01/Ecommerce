package com.shopping.bloom.model.newfragment;

import com.shopping.bloom.model.Category;

import java.util.List;

public class NewTrend {

    private String imageUrl;
    private String title;
    private String description;
    private int background;
    private List<Category> categoryList;

    public NewTrend() {
    }

    public NewTrend(String imageUrl, String title, String description, int background, List<Category> categoryList) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.background = background;
        this.categoryList = categoryList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
