package com.shopping.bloom.model.myorders;

public class Order {

    private String orderId;
    private String orderName;
    private String paymentMethod;
    private String deliveryDate;
    private String orderPrice;
    private String orderImage;

    public Order(String orderId, String orderName, String paymentMethod, String deliveryDate, String orderPrice, String orderImage) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.paymentMethod = paymentMethod;
        this.deliveryDate = deliveryDate;
        this.orderPrice = orderPrice;
        this.orderImage = orderImage;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }
}
