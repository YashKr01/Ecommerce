package com.shopping.bloom.models;

public class DummyDataModel {

    private int image;
    private String cost;

    public DummyDataModel() {

    }

    public DummyDataModel(int image, String cost) {
        this.image = image;
        this.cost = cost;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
