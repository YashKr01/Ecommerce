package com.shopping.bloom.model.fragmentnew;

import java.util.List;

public class NewTrends {

    private String imageUrl;
    private String title;
    private String description;
    private int background;
    private List<Child> childList;

    public NewTrends() {
    }

    public NewTrends(String imageUrl, String title, String description, int background, List<Child> childList) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.background = background;
        this.childList = childList;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
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
