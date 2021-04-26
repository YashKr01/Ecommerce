package com.shopping.bloom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressDataResponse {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("user_id")
    @Expose
    String user_id;

    @SerializedName("address_name")
    @Expose
    String address_name;

    @SerializedName("is_primary")
    @Expose
    String is_primary;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(String is_primary) {
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
