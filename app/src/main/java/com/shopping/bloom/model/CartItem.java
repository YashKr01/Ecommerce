package com.shopping.bloom.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_item")
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String parentId;
    private String childId;
    private String name;
    private String primaryImage;
    private String color;
    private String size;
    private String productPrice;
    private int quantity;

    public CartItem(String parentId, String childId, String name, String primaryImage, String color, String size, String productPrice) {
        this.id = 0;
        this.parentId = parentId;
        this.childId = childId;
        this.name = name;
        this.primaryImage = primaryImage;
        this.color = color;
        this.size = size;
        this.productPrice = productPrice;
        this.quantity = 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getName() {
        return name;
    }

    public void setName(String productName) {
        this.name = productName;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
