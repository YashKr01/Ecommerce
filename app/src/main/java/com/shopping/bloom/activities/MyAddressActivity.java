package com.shopping.bloom.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.App;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.AddressAdapter;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.model.AddressDataResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.AddressClickListener;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.MyAddressViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.bloom.utils.Const.ADD_ADDRESS_ACTIVITY;

public class MyAddressActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    AddressAdapter addressAdapter;
    List<AddressDataResponse> addressList;
    MyAddressViewModel myAddressViewModel;
    AddressDataResponse addressDataResponse;
    String CALLING_ACTIVITY = "";
    LoginManager loginManager;
    int pos;
    RelativeLayout relativeLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    ViewStub viewStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        getIntentData();
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        relativeLayout = findViewById(R.id.relativeLayout);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        addressList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        addressAdapter = new AddressAdapter(this, addressList, getApplication(), clickListener);

        recyclerView.setAdapter(addressAdapter);
        loginManager = new LoginManager(App.getContext());

        checkNetworkConnectivity();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            checkNetworkConnectivity();
            myAddressViewModel.makeApiCall(0, 10, getApplication());
        });

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

        myAddressViewModel.getUpdateLiveData().observe(this, loginResponseModel -> {
            if (loginResponseModel == null) {
                System.out.println("Null response");
            } else {
                if (loginResponseModel.getSuccess().equals("true")) {
                    loginManager.setPrimary_address_id(addressDataResponse.getId());
                    loginManager.setPrimaryAddress(addressDataResponse.getAddress_name() + "," + addressDataResponse.getAddress_line_1() + "," + addressDataResponse.getCity() + "," + addressDataResponse.getPincode() + "," + addressDataResponse.getContact_number());
                    loginManager.setIs_primary_address_available(true);
                    Toast.makeText(MyAddressActivity.this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        myAddressViewModel.getDeleteLiveData().observe(MyAddressActivity.this, loginResponseModel -> {

            if (addressList.get(pos).getIs_primary().equals("1")) {
                loginManager.setPrimary_address_id("NA");
                loginManager.setPrimaryAddress("NA");
                loginManager.setIs_primary_address_available(false);
                addressAdapter.notifyDataSetChanged();
            }
            addressList.remove(pos);
            addressAdapter.notifyDataSetChanged();
        });


        setNavigationIcon();

    }

    private void getIntentData() {
        Intent data = getIntent();
        if (data != null) {
            String ARG_CALLING_ACTIVITY = "CALLING_ACTIVITY";
            CALLING_ACTIVITY = data.getStringExtra(ARG_CALLING_ACTIVITY);
        }
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

    private final AddressClickListener clickListener = new AddressClickListener() {
        @Override
        public void onAddressClicked(AddressDataResponse address) {
            if (CALLING_ACTIVITY == null || CALLING_ACTIVITY.isEmpty()) {
                return;
            }
            String ARG_ADDRESS_ID = "ADDRESS_ID";
            String ARG_ADDRESS = "ADDRESS";
            if (CALLING_ACTIVITY.equals(CheckoutActivity.class.getName())) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ARG_ADDRESS_ID, address.getId());
                String buildAddress = (address.getAddress_name() + "," +
                        address.getAddress_line_1() + "," +
                        address.getCity() + "," +
                        address.getPincode() + "," +
                        address.getContact_number());
                returnIntent.putExtra(ARG_ADDRESS, buildAddress);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }

        @Override
        public void onAddressUpdate(AddressDataResponse address) {
            //TODO: Update address
            Intent intent = new Intent(getApplicationContext(), UpdateAddressActivity.class);
            intent.putExtra("addressName", address.getAddress_name());
            intent.putExtra("addressLine", address.getAddress_line_1());
            intent.putExtra("city", address.getCity());
            intent.putExtra("pinCode", address.getPincode());
            intent.putExtra("number", address.getContact_number());
            intent.putExtra("is_primary", address.getIs_primary());
            intent.putExtra("id", address.getId());
            startActivity(intent);
        }

        @Override
        public void onAddressDelete(AddressDataResponse address, int position) {
            //TODO: delete address
            pos = position;
            myAddressViewModel.delete(address, getApplication());

        }

        @Override
        public void setAsDefault(AddressDataResponse address) {
            //TODO: Update the default address here:

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ADDRESS_ACTIVITY && resultCode == RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }

    private void checkNetworkConnectivity() {
        swipeRefreshLayout.setRefreshing(false);
        if (!NetworkCheck.isConnect(this)) {
            viewStub.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        } else {
            viewStub.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }
}