package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.chaos.view.PinView;
import com.shopping.bloom.R;
import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.OtpViewModel;


public class OtpActivity extends AppCompatActivity {

    Toolbar toolbar;
    PinView pinView;
    String otp;
    OtpViewModel otpViewModel;
    ShowToast showToast;
    String activityName;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        activityName = getIntent().getStringExtra("activityName");
        System.out.println("ActivityName" + activityName);

        showToast = new ShowToast(this);

        toolbar = findViewById(R.id.toolbar);
        setNavigationIcon();

        pinView = findViewById(R.id.pinView);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);

        otpViewModel = ViewModelProviders.of(this).get(OtpViewModel.class);
        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();
    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void resendOtp(View view) {
        if (activityName.equals("EmailVerification")) {
            String email = getIntent().getStringExtra("email");
            if (email == null || email.isEmpty()) {
                showToast.showToast("Email is Empty");
            } else {
                EmailVerificationModel emailVerificationModel = new EmailVerificationModel(email);
                otpViewModel.makeApiCallResendEmailOtp(emailVerificationModel, getApplication(), this);
            }
        } else {
            String mobile_no = getIntent().getStringExtra("mobile_no");
            if (mobile_no == null || mobile_no.isEmpty()) {
                showToast.showToast("Number is Empty");
            } else {
                if (NetworkCheck.isConnect(this)) {
                    LoginModel loginModel = new LoginModel(mobile_no);
                    otpViewModel.makeApiCallResendOtp(loginModel, getApplication(), this);
                } else {
                    viewStub.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.GONE);
                }
            }
        }

    }

    public void verifyOtp(View view) {
        otp = pinView.getText().toString().trim();
        String activityName = getIntent().getStringExtra("activityName");

        if (otp == null || otp.isEmpty()) {
            showToast.showToast("Number is Empty");
        } else {
            if(NetworkCheck.isConnect(this)){
                if (activityName.equals("EmailVerification")) {
                    EmailOtpModel otpModel = new EmailOtpModel(getIntent().getStringExtra("email"), otp);
                    otpViewModel.makeApiCallVerifyEmailOtp(otpModel, getApplication(), this, activityName);
                } else {
                    OtpModel otpModel = new OtpModel(getIntent().getStringExtra("mobile_no"), otp);
                    otpViewModel.makeApiCallVerifyOtp(otpModel, getApplication(), this, activityName);
                }

            }else{
                viewStub.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activityName.equals("EmailVerification")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkNetworkConnectivity() {

        if (!NetworkCheck.isConnect(this)) {
            viewStub.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
        } else {
            viewStub.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);

    }
}