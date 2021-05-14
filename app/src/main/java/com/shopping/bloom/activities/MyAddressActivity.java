package com.shopping.bloom.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shopping.bloom.App;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.AddressAdapter;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.model.AddressDataResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.viewModels.MyAddressViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.shopping.bloom.utils.Const.ADD_ADDRESS_ACTIVITY;

public class MyAddressActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    AddressAdapter addressAdapter;
    List<AddressDataResponse> addressList;
    MyAddressViewModel myAddressViewModel;
    AddressDataResponse addressDataResponse;
    int id;

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
                if (addressDataResponse != null) {
                    addressList = addressDataResponse;
                    addressAdapter.setAddressList(addressList);
                    addressAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MyAddressActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
        myAddressViewModel.makeApiCall(0, 10, getApplication());

        myAddressViewModel.getUpdateLiveData().observe(this, new Observer<LoginResponseModel>() {
            @Override
            public void onChanged(LoginResponseModel loginResponseModel) {
                if (loginResponseModel == null) {
                    System.out.println("Null response");
                } else {
                    if (loginResponseModel.getSuccess().equals("true")) {
                        LoginManager loginManager = new LoginManager(App.getContext());
                        loginManager.setPrimary_address_id(addressDataResponse.getId());
                        loginManager.setPrimaryAddress(addressDataResponse.getAddress_name() + "," + addressDataResponse.getAddress_line_1() + "," + addressDataResponse.getCity() + "," + addressDataResponse.getPincode() + "," + addressDataResponse.getContact_number());
                        loginManager.setIs_primary_address_available(true);
                        Toast.makeText(MyAddressActivity.this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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
        startActivityForResult(intent, ADD_ADDRESS_ACTIVITY);
    }

    public void getData(int pos) {
        addressDataResponse = addressList.get(pos);
        AddAddressModel addAddressModel = new AddAddressModel(addressDataResponse.getAddress_name(), 1, addressDataResponse.getPincode(), addressDataResponse.getAddress_line_1(), addressDataResponse.getCity(), addressDataResponse.getContact_number());
        myAddressViewModel.updateAddressApiCall(addressDataResponse.getId(), addAddressModel, getApplication());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ADDRESS_ACTIVITY && resultCode == RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }
}