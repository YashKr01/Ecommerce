package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.AddressAdapter;
import com.shopping.bloom.model.Address;

import java.util.ArrayList;
import java.util.List;

public class MyAddressActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    AddressAdapter addressAdapter;
    List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        addressList = new ArrayList<>();
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        addressList.add(new Address("demoName1", "Address line 1", "Address line 3"));
        addressList.add(new Address("demoName2", "Address line 2", "Address line 3"));
        addressList.add(new Address("demoName3", "Address line 3", "Address line 3"));
        addressAdapter = new AddressAdapter(this,addressList);
        recyclerView.setAdapter(addressAdapter);

        setNavigationIcon();

    }
    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    public void addShippingAddress(View view) {
        Intent intent = new Intent(this, AddShippingAddressActivity.class);
        startActivity(intent);
    }
}