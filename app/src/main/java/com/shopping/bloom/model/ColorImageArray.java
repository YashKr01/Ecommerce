package com.shopping.bloom.model;

public class ColorImageArray {

    private String color;
    private String imagePath;

    public ColorImageArray(String color, String imagePath) {
        this.color = color;
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ColorImageArray{" +
                "color='" + color + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
