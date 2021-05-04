package com.shopping.bloom.model.shoppingbag;

import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_bag")
public class ProductEntity {

    @PrimaryKey(autoGenerate = false)
    private Integer id;
    private String productImage;
    private String productName;
    private String productSize;
    private String productPrice;
    private String color;

    public ProductEntity(Integer id, String productImage, String productName, String productSize, String productPrice, String color) {
        this.id = id;
        this.productImage = productImage;
        this.productName = productName;
        this.productSize = productSize;
        this.productPrice = productPrice;
        this.color = color;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
