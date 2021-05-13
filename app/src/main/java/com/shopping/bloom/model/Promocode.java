package com.shopping.bloom.model;

public class Promocode {
    public int id;
    public String promocode;
    public String type;
    public String discount;
    public String minimal_cart_total;
    public String max_discount;
    public String is_for_new_user;
    public String created_at;
    public String updated_at;
    public String start_from;
    public String end_on;
    public String description;
    public String is_active;

    public Promocode(int id, String promocode, String type, String discount, String minimal_cart_total,
                     String max_discount, String is_for_new_user, String created_at, String updated_at,
                     String start_from, String end_on, String description, String is_active) {
        this.id = id;
        this.promocode = promocode;
        this.type = type;
        this.discount = discount;
        this.minimal_cart_total = minimal_cart_total;
        this.max_discount = max_discount;
        this.is_for_new_user = is_for_new_user;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.start_from = start_from;
        this.end_on = end_on;
        this.description = description;
        this.is_active = is_active;
    }
}
