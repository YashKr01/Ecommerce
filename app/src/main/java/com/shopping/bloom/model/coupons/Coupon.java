package com.shopping.bloom.model.coupons;

public class Coupon {

    private String status;
    private String rate;
    private String code;
    private String orders;
    private String date;
    private String products;

    public Coupon(String status, String rate, String code, String orders, String date, String products) {
        this.status = status;
        this.rate = rate;
        this.code = code;
        this.orders = orders;
        this.date = date;
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
