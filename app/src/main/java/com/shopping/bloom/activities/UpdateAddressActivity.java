package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.shopping.bloom.R;
import com.shopping.bloom.model.AddAddressModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.viewModels.UpdateAddressViewModel;

public class UpdateAddressActivity extends AppCompatActivity {

    EditText addressNameEditText, addressLineEditText, cityEditText, pinCodeEditText, contactEditText;
    Toolbar toolbar;
    Button button;
    CheckBox checkBox;
    int is_primary = 0;
    String id;
    private View parent_view;
    AddAddressModel addressModel;
    UpdateAddressViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        toolbar = findViewById(R.id.toolbar);

        addressLineEditText = findViewById(R.id.addressLineEditText);
        addressNameEditText = findViewById(R.id.addressNameEditText);
        cityEditText = findViewById(R.id.cityTextView);
        pinCodeEditText = findViewById(R.id.pinCodeTextView);
        contactEditText = findViewById(R.id.numberEditText);
        parent_view = findViewById(android.R.id.content);

        checkBox = findViewById(R.id.radio);

        addressNameEditText.setText(getIntent().getStringExtra("addressName"));
        addressLineEditText.setText(getIntent().getStringExtra("addressLine"));
        cityEditText.setText(getIntent().getStringExtra("city"));
        pinCodeEditText.setText(getIntent().getStringExtra("pinCode"));
        contactEditText.setText(getIntent().getStringExtra("number"));

        id = getIntent().getStringExtra("id");

        checkBox.setChecked(getIntent().getStringExtra("is_primary").equals("1"));

        System.out.println("is_primary = " + getIntent().getStringExtra("is_primary"));

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

        viewModel = ViewModelProviders.of(this).get(UpdateAddressViewModel.class);
        viewModel.getMutableLiveData().observe(this, loginResponseModel -> {
            if (loginResponseModel.getSuccess().equals("true")) {
                Toast.makeText(this, loginResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MyAddressActivity.class);
                startActivity(intent);
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
                } else if (pinCode.length() != 6) {
                    Snackbar.make(parent_view, "Pin code length should be 6.", Snackbar.LENGTH_SHORT).show();
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
        viewModel.makeApiCall(id, addressModel, getApplication());
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