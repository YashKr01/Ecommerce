package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shopping.bloom.R;
import com.shopping.bloom.databinding.ActivitySingleProductBinding;

public class SingleProductActivity extends AppCompatActivity {

    private ActivitySingleProductBinding binding;
    private Integer PRODUCT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingleProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PRODUCT_ID = getIntent().getIntExtra("PRODUCT_ID", 1);

        Log.d("SEND", "onCreate: "+PRODUCT_ID);


    }
}