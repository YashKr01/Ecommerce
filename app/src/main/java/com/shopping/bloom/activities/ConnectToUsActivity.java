package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.shopping.bloom.R;

public class ConnectToUsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_us);

        toolbar = findViewById(R.id.toolbar);

        setNavigationIcon();
    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}