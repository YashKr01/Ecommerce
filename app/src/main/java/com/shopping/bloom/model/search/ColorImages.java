package com.shopping.bloom.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ColorImages {

    @Expose
    @SerializedName("color")
    private String color;
    @Expose
    @SerializedName("imagePath")
    private String imagePath;

    public String getColor() {
        return color;
    }

    public String getImagePath() {
        return imagePath;
    }

}
