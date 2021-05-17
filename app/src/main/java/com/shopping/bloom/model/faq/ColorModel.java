package com.shopping.bloom.model.faq;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class ColorModel {
    String name;
    String hex_value;

    public ColorModel(String name, String hex_value) {
        this.name = name;
        this.hex_value = hex_value;
    }

    public String getName() {
        return name;
    }

    public String getHexValue() {
        return hex_value;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "name= "+name+" hexValue= "+hex_value;
    }
}
