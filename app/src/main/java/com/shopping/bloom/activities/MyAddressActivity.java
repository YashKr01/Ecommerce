package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.AddressAdapter;
import com.shopping.bloom.model.AddressDataResponse;
import com.shopping.bloom.viewModels.MyAddressViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyAddressActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    AddressAdapter addressAdapter;
    List<AddressDataResponse> addressList;
    MyAddressViewModel myAddressViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        addressList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        addressAdapter = new AddressAdapter(this, addressList, getApplication());
        recyclerView.setAdapter(addressAdapter);

        myAddressViewModel = ViewModelProviders.of(this).get(MyAddressViewModel.class);

        myAddressViewModel.getMutableLiveData().observe(this, new Observer<List<AddressDataResponse>>() {
            @Override
            public void onChanged(List<AddressDataResponse> addressDataResponse) {
                addressList = addressDataResponse;
                addressAdapter.setAddressList(addressList);
                addressAdapter.notifyDataSetChanged();
            }
        });
        myAddressViewModel.makeApiCall(0, 10, getApplication());

        setNavigationIcon();

    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        });
    }


    public void addShippingAddress(View view) {
        Intent intent = new Intent(this, AddShippingAddressActivity.class);
        startActivity(intent);
    }
}