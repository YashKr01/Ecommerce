package com.shopping.bloom.model.giftcard;

public class GiftCard {

    private String amount;
    private String validity;

    public GiftCard(String amount, String validity) {
        this.amount = amount;
        this.validity = validity;
    }

    public String getAmount() {
        return amount;
    }

    public String getValidity() {
        return validity;
    }

}
