package com.shopping.bloom.model.myorders;

import com.shopping.bloom.model.Product;

import java.util.List;

public class MyOrders {

    private String status;
    private List<Order> orderList;

    public MyOrders(String status, List<Order> orderList) {
        this.status = status;
        this.orderList = orderList;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

}
