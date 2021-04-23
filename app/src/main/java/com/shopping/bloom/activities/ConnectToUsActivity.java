package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shopping.bloom.R;
import com.shopping.bloom.utils.DebouncedOnClickListener;

public class ConnectToUsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button orderIssue, delivery, refund, payment, product, account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_us);

        toolbar = findViewById(R.id.toolbar);
        orderIssue = findViewById(R.id.orderIssue);
        delivery = findViewById(R.id.delivery);
        refund = findViewById(R.id.refund);
        payment = findViewById(R.id.payment);
        product = findViewById(R.id.product);
        account = findViewById(R.id.account);

        setNavigationIcon();

        orderIssue.setOnClickListener(debouncedOnClickListener);
        delivery.setOnClickListener(debouncedOnClickListener);
        refund.setOnClickListener(debouncedOnClickListener);
        payment.setOnClickListener(debouncedOnClickListener);
        product.setOnClickListener(debouncedOnClickListener);
        account.setOnClickListener(debouncedOnClickListener);
    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FAQActivity.class);
            if(v.getId() == R.id.orderIssue){
                intent.putExtra("Title", "Order issues");
                startActivity(intent);
            }else if(v.getId() == R.id.delivery){
                intent.putExtra("Title", "Delivery");
                startActivity(intent);
            }else if(v.getId() == R.id.refund){
                intent.putExtra("Title", "Return & Refund");
                startActivity(intent);
            }else if(v.getId() == R.id.payment){
                intent.putExtra("Title", "Payment & Promos");
                startActivity(intent);
            }else if(v.getId() == R.id.product){
                intent.putExtra("Title", "Product & Stock");
                startActivity(intent);
            }else if(v.getId() == R.id.account){
                intent.putExtra("Title", "Account");
                startActivity(intent);
            }
        }
    };

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }
}