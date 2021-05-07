package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shopping.bloom.R;

public class SeeMoreProductsActivity extends AppCompatActivity {
    private static final String TAG = SeeMoreProductsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more_products);

        initViews();
        setUpRecyclerView();
        getIntentData();

    }

    private void initViews() {

    }

    private void setUpRecyclerView() {

    }

    private void getIntentData() {

    }



}