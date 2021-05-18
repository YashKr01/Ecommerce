package com.shopping.bloom.utils;

public class Const {

    //Base URLs
    public static final String GET_BASE_URL = "http://bloomapp.in/";

    public static final int CONNECTION_TIMEOUT = 20;
    public static final int READ_TIMEOUT = 20;
    public static final int WRITE_TIMEOUT = 20;

    public static final int LOGIN_ACTIVITY = 100;
    public static final int ADD_ADDRESS_ACTIVITY = 101;
    public static final int OTP_ACTIVITY = 102;


    public enum SORT_BY {
        NEW_ARRIVAL,
        MOST_POPULAR,
        PRICE_HIGH_TO_LOW,
        PRICE_LOW_TO_HIGH,
        FILTERS,
    }

    public enum FILTER {
        LENGTH,
        TYPE,
        COLOR,
        CATEGORY
    }
}
