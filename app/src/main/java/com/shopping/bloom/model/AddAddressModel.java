package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAddressModel {

    @SerializedName("address_name")
    @Expose
    String address_name;

    @SerializedName("is_primary")
    @Expose
    int is_primary;

    @SerializedName("pincode")
    @Expose
    String pincode;

    @SerializedName("address_line_1")
    @Expose
    String address_line_1;

    @SerializedName("city")
    @Expose
    String city;

    @SerializedName("contact_number")
    @Expose
    String contact_number;

    public AddAddressModel(String address_name, int is_primary, String pincode, String address_line_1, String city, String contact_number) {
        this.address_name = address_name;
        this.is_primary = is_primary;
        this.pincode = pincode;
        this.address_line_1 = address_line_1;
        this.city = city;
        this.contact_number = contact_number;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public int getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(int is_primary) {
        this.is_primary = is_primary;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
