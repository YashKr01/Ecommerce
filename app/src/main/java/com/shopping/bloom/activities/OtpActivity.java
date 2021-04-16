package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.shopping.bloom.R;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewmodel.OtpViewModel;
import com.shopping.bloom.viewmodel.RegisterViewModel;

public class OtpActivity extends AppCompatActivity {

    Toolbar toolbar;
    PinView pinView;
    String otp;
    OtpViewModel otpViewModel;
    ShowToast showToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        showToast = new ShowToast(this);

        toolbar = findViewById(R.id.toolbar);
        setNavigationIcon();

        pinView = findViewById(R.id.pinView);

        otpViewModel = ViewModelProviders.of(this).get(OtpViewModel.class);
    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void resendOtp(View view) {
        otp = pinView.getText().toString().trim();

        if (otp == null || otp.isEmpty()) {
            showToast.showToast("Number is Empty");
        } else {
            otpViewModel.makeApiCallResendOtp(otp);
        }
    }

    public void verifyOtp(View view) {
        otp = pinView.getText().toString().trim();

        if (otp == null || otp.isEmpty()) {
            showToast.showToast("Number is Empty");
        } else {
            OtpModel otpModel = new OtpModel(getIntent().getStringExtra("mobile_no"), otp);
            otpViewModel.makeApiCallVerifyOtp(otpModel);
        }
    }
}