package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.shopping.bloom.R;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.model.AddressDataResponse;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.AddShippingAddressViewModel;

public class AddShippingAddressActivity extends AppCompatActivity {

    EditText fullName, addressNameEditText, addressLineEditText, cityEditText, pinCodeEditText, contactEditText;
    TextInputLayout nameTextInputLayout, addressTextInputLayout2, cityTextInputLayout3,
            pinTextInputLayout4, numberTextInputLayout5, fullNameTextInputLayout;
    Toolbar toolbar;
    Button button, cancelButton;
    CheckBox checkBox;
    int is_primary = 0;
    ViewStub viewStub;
    View inflated;
    ScrollView scrollView;
    private View parent_view;
    LoginManager loginManager;
    AddAddressModel addressModel;
    AddShippingAddressViewModel addShippingAddressViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipping_address);
        toolbar = findViewById(R.id.toolbar);

        addressLineEditText = findViewById(R.id.addressLineEditText);
        addressNameEditText = findViewById(R.id.addressNameEditText);
        cityEditText = findViewById(R.id.cityTextView);
        pinCodeEditText = findViewById(R.id.pinCodeTextView);
        contactEditText = findViewById(R.id.numberEditText);
        parent_view = findViewById(android.R.id.content);
        viewStub = findViewById(R.id.vsEmptyScreen);
        inflated = viewStub.inflate();
        scrollView = findViewById(R.id.scrollView);
        fullName = findViewById(R.id.fullNameEditText);

        checkBox = findViewById(R.id.radio);

        fullNameTextInputLayout = findViewById(R.id.fullNameTextInputLayout);
        nameTextInputLayout = findViewById(R.id.textInputLayout2);
        addressTextInputLayout2 = findViewById(R.id.textInputLayout3);
        cityTextInputLayout3 = findViewById(R.id.textInputLayout4);
        pinTextInputLayout4 = findViewById(R.id.textInputLayout5);
        numberTextInputLayout5 = findViewById(R.id.textInputLayout6);

        button = findViewById(R.id.addAddressButton);
        cancelButton = findViewById(R.id.cancelButton);
        button.setOnClickListener(debouncedOnClickListener);
        cancelButton.setOnClickListener(debouncedOnClickListener);

        loginManager = new LoginManager(this);

        if (checkNetworkConnectivity()) {
            scrollView.setVisibility(View.VISIBLE);
            viewStub.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.GONE);
            viewStub.setVisibility(View.VISIBLE);
        }

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                is_primary = 1;
            } else {
                is_primary = 0;
            }
        });


        button.setOnClickListener(debouncedOnClickListener);
        setNavigationIcon();

        addShippingAddressViewModel = ViewModelProviders.of(this).get(AddShippingAddressViewModel.class);
        addShippingAddressViewModel.getMutableLiveData().observe(this, addAddressResponse -> {

            if (addAddressResponse != null) {

                if (addAddressResponse.getSuccess().equals("true")) {
                    Toast.makeText(this, addAddressResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    for (AddressDataResponse addressDataResponse : addAddressResponse.getAddressDataResponseList()) {

                        if (addressDataResponse.getIs_primary().equals("1")) {
                            loginManager.setPrimary_address_id(addressDataResponse.getId());
                            loginManager.setPrimaryAddress(addressDataResponse.getAddress_name() + "," + addressDataResponse.getAddress_line_1() + "," + addressDataResponse.getCity() + "," + addressDataResponse.getPincode() + "," + addressDataResponse.getContact_number());
                            loginManager.setIs_primary_address_available(true);
                        }
                    }

                    //goes to specific activity
                    Intent resultIntent = getIntent();

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }
            }
        });


    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.addAddressButton) {
                String name = fullName.getText().toString().trim();
                String addressName = addressNameEditText.getText().toString().trim();
                String addressLine = addressLineEditText.getText().toString().trim();
                String city = cityEditText.getText().toString().trim();
                String pinCode = pinCodeEditText.getText().toString().trim();
                String number = contactEditText.getText().toString().trim();

                if (name.isEmpty()) {
                    fullNameTextInputLayout.setErrorEnabled(true);
                    fullNameTextInputLayout.setError("Please Enter Full Name");
                } else if (addressName.isEmpty()) {
                    nameTextInputLayout.setErrorEnabled(true);
                    nameTextInputLayout.setError("Please Enter Address Name");
                } else if (addressLine.isEmpty()) {
                    addressTextInputLayout2.setErrorEnabled(true);
                    addressTextInputLayout2.setError("Please Enter Address");
                } else if (city.isEmpty()) {
                    cityTextInputLayout3.setErrorEnabled(true);
                    cityTextInputLayout3.setError("Please Enter City Name");
                } else if (pinCode.isEmpty()) {
                    pinTextInputLayout4.setErrorEnabled(true);
                    pinTextInputLayout4.setError("Please Enter Pin code");
                } else if (pinCode.length() != 6) {
                    pinTextInputLayout4.setErrorEnabled(true);
                    pinTextInputLayout4.setError("Incorrect Pin");
                } else if (number.isEmpty()) {
                    numberTextInputLayout5.setErrorEnabled(true);
                    numberTextInputLayout5.setError("Please Enter your contact details");
                } else if (!numberLength(number)) {
                    numberTextInputLayout5.setErrorEnabled(true);
                    numberTextInputLayout5.setError("Incorrect Contact Number");
                } else {
                    fullNameTextInputLayout.setErrorEnabled(false);
                    fullNameTextInputLayout.setError(null);

                    nameTextInputLayout.setErrorEnabled(false);
                    nameTextInputLayout.setError(null);

                    addressTextInputLayout2.setErrorEnabled(false);
                    addressTextInputLayout2.setError(null);

                    cityTextInputLayout3.setErrorEnabled(false);
                    cityTextInputLayout3.setError(null);

                    pinTextInputLayout4.setErrorEnabled(false);
                    pinTextInputLayout4.setError(null);

                    numberTextInputLayout5.setErrorEnabled(false);
                    numberTextInputLayout5.setError(null);
                    if (checkNetworkConnectivity()) {
                        scrollView.setVisibility(View.VISIBLE);
                        viewStub.setVisibility(View.GONE);
                        addAddress(addressName, addressLine, city, pinCode, number, is_primary);
                    } else {
                        scrollView.setVisibility(View.GONE);
                        viewStub.setVisibility(View.VISIBLE);

                        //setting up no internet check
                        TextView textViewVS = inflated.findViewById(R.id.tvSwipeToRefresh);
                        textViewVS.setText("Click to Refresh");
                        ConstraintLayout constraintLayout = inflated.findViewById(R.id.constraintLayout);
                        constraintLayout.setOnClickListener(new DebouncedOnClickListener(150) {
                            @Override
                            public void onDebouncedClick(View v) {
                                if (checkNetworkConnectivity()) {
                                    scrollView.setVisibility(View.VISIBLE);
                                    viewStub.setVisibility(View.GONE);
                                } else {
                                    scrollView.setVisibility(View.GONE);
                                    viewStub.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            }else if(v.getId() == R.id.cancelButton){
                finish();
            }
        }
    };

    private void addAddress(String addressName, String addressLine, String city, String pinCode, String phoneNumber, int is_primary) {
        addressModel = new AddAddressModel(addressName, is_primary, pinCode, addressLine, city, phoneNumber);
        addShippingAddressViewModel.makeApiCall(addressModel, getApplication());
    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private boolean numberLength(String number) {
        return number.length() == 10;
    }

    private boolean checkNetworkConnectivity() {
        return NetworkCheck.isConnect(this);
    }
}