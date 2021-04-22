package com.shopping.bloom.utils;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {
    Context context;
//    public ShowToast(Context context){
//        this.context = context;
//    }
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
