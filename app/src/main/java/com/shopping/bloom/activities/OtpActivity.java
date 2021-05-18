package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.shopping.bloom.R;
import com.shopping.bloom.model.EmailOtpModel;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.OtpModel;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.OtpViewModel;


public class OtpActivity extends AppCompatActivity {

    Toolbar toolbar;
    PinView pinView;
    String otp;
    OtpViewModel otpViewModel;
    String activityName;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    SwipeRefreshLayout swipeRefreshLayout;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        loginManager = new LoginManager(this);

        activityName = getIntent().getStringExtra("activityName");
        System.out.println("ActivityName" + activityName);

        toolbar = findViewById(R.id.toolbar);
        setNavigationIcon();

        pinView = findViewById(R.id.pinView);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);

        otpViewModel = ViewModelProviders.of(this).get(OtpViewModel.class);
        otpViewModel.getMutableLiveData().observe(this, otpResponseModel -> {
            if (otpResponseModel != null) {
                String success = otpResponseModel.getSuccess();
                String message = otpResponseModel.getMessage();
                if (success.equals("true")) {

                    ShowToast.showToast(this, message);

                    if (activityName.equals("LoginActivity")) {

                        loginManager.setEmailid(otpResponseModel.getData().getUserInfo().getEmail());
                        loginManager.setname(otpResponseModel.getData().getUserInfo().getName());
                        loginManager.setNumber(otpResponseModel.getData().getUserInfo().getMobile_no());
                        loginManager.setFirebase_token(otpResponseModel.getData().getUserInfo().getFirebase_token());
                        loginManager.settoken(otpResponseModel.getData().getToken());
                        loginManager.SetLoginStatus(false);
                        String email_verified_at = otpResponseModel.getData().getUserInfo().getEmail_verified_at();

                        loginManager.setEmail_verified_at(email_verified_at != null && !email_verified_at.isEmpty() && !email_verified_at.equals(""));

                    }
                    Intent resultIntent = getIntent();

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
//                    Intent intent;
//                    if (activityName.equals("RegisterActivity")) {
//                        intent = new Intent(this, LoginActivity.class);
//                    } else {
//                        intent = new Intent(this, MainActivity.class);
//                        loginManager.SetLoginStatus(false);
//                    }
//                    startActivity(intent);
//                    finish();

                } else {
                    ShowToast.showToast(this, message);
                }
            } else {
                ShowToast.showToast(this, "Otp Verification Failed");
            }
        });

        otpViewModel.getEmailModelMutableLiveData().observe(this, otpResponseModel -> {
            if (otpResponseModel != null) {
                String success = otpResponseModel.getSuccess();
                String message = otpResponseModel.getMessage();
                if (success.equals("true")) {
                    ShowToast.showToast(this, message);

                    loginManager.setEmailid(otpResponseModel.getData().getUserInfo().getEmail());
                    loginManager.setname(otpResponseModel.getData().getUserInfo().getName());
                    loginManager.setNumber(otpResponseModel.getData().getUserInfo().getMobile_no());
                    loginManager.setFirebase_token(otpResponseModel.getData().getUserInfo().getFirebase_token());
                    loginManager.settoken(otpResponseModel.getData().getToken());

                    String email_verified_at = otpResponseModel.getData().getUserInfo().getEmail_verified_at();
                    if (email_verified_at != null || !email_verified_at.isEmpty() || !email_verified_at.equals("")) {
                        loginManager.setEmail_verified_at(true);
                    }

                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    ShowToast.showToast(this, message);
                }
            } else {
                ShowToast.showToast(this, "Otp Verification Failed");
            }
        });

        otpViewModel.getLoginResponseModelMutableLiveData().observe(this, loginResponseModel -> {
            if (loginResponseModel != null) {
                ShowToast.showToast(this, loginResponseModel.getMessage());
            } else {
                ShowToast.showToast(this, "Failed");
            }
        });

        otpViewModel.getEmailResponseMutableLiveData().observe(this, emailVerificationResponse -> {
            if (emailVerificationResponse != null) {
                ShowToast.showToast(this, emailVerificationResponse.getMessage());
            } else {
                ShowToast.showToast(this, "Failed");
            }
        });

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
                ShowToast.showToast(this, "Email is Empty");
            } else {
                if (NetworkCheck.isConnect(this)) {
                    EmailVerificationModel emailVerificationModel = new EmailVerificationModel(email);
                    otpViewModel.makeApiCallResendEmailOtp(emailVerificationModel, getApplication(), this);
                }else {
                    viewStub.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.GONE);
                }
            }
        } else {
            String mobile_no = getIntent().getStringExtra("mobile_no");
            if (mobile_no == null || mobile_no.isEmpty()) {
                ShowToast.showToast(this, "Number is Empty");
            } else {
                if (NetworkCheck.isConnect(this)) {
                    LoginModel loginModel = new LoginModel(mobile_no);
                    otpViewModel.makeApiCallResendOtp(loginModel, getApplication());
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
            ShowToast.showToast(this, "Number is Empty");
        } else {
            if (NetworkCheck.isConnect(this)) {
                if (activityName.equals("EmailVerification")) {
                    EmailOtpModel otpModel = new EmailOtpModel(getIntent().getStringExtra("email"), otp);
                    otpViewModel.makeApiCallVerifyEmailOtp(otpModel, getApplication(), this);
                } else {
                    OtpModel otpModel = new OtpModel(getIntent().getStringExtra("mobile_no"), otp);
                    otpViewModel.makeApiCallVerifyOtp(otpModel, getApplication(), this);
                }

            } else {
                viewStub.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        if (activityName.equals("EmailVerification")) {
            intent = new Intent(this, SettingsActivity.class);
        }else{
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        }
        startActivity(intent);
        finish();
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