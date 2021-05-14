package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.shopping.bloom.R;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.viewModels.AddShippingAddressViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddShippingAddressActivity extends AppCompatActivity {

    EditText addressNameEditText, addressLineEditText, cityEditText, pinCodeEditText, contactEditText;
    Toolbar toolbar;
    Button button;
    CheckBox checkBox;
    int is_primary = 0;
    private View parent_view;
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

        checkBox = findViewById(R.id.radio);

        button = findViewById(R.id.addAddressButton);
        button.setOnClickListener(debouncedOnClickListener);

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
        addShippingAddressViewModel.getMutableLiveData().observe(this, loginResponseModel -> {
            if (loginResponseModel.getSuccess().equals("true")) {
                Toast.makeText(this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();

                //goes to specific activity
                Intent resultIntent = getIntent();

                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });

    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.addAddressButton) {
                String addressName = addressNameEditText.getText().toString().trim();
                String addressLine = addressLineEditText.getText().toString().trim();
                String city = cityEditText.getText().toString().trim();
                String pinCode = pinCodeEditText.getText().toString().trim();
                String number = contactEditText.getText().toString().trim();

                if (addressName == null || addressName.isEmpty()) {
                    Snackbar.make(parent_view, "Address Name is Empty", Snackbar.LENGTH_SHORT).show();
                } else if (addressLine == null || addressLine.isEmpty()) {
                    Snackbar.make(parent_view, "Address Line is Empty", Snackbar.LENGTH_SHORT).show();
                } else if (city == null || city.isEmpty()) {
                    Snackbar.make(parent_view, "City Name is Empty", Snackbar.LENGTH_SHORT).show();
                } else if (pinCode == null || pinCode.isEmpty()) {
                    Snackbar.make(parent_view, "Pin Code is Empty", Snackbar.LENGTH_SHORT).show();
                } else if (number == null || number.isEmpty()) {
                    Snackbar.make(parent_view, "Mobile No. is Empty", Snackbar.LENGTH_SHORT).show();
                } else if (!numberLength(number)) {
                    Snackbar.make(parent_view, "Mobile No. length should be 10.", Snackbar.LENGTH_SHORT).show();
                } else {
                    addAddress(addressName, addressLine, city, pinCode, number, is_primary);
                }
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
}