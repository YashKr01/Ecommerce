package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shopping.bloom.R;
import com.shopping.bloom.fragment.changepassword.ChangePasswordFragment;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.AccountSecurityViewModel;

public class AccountSecurityActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView emailVerificationTextView;
    AccountSecurityViewModel accountSecurityViewModel;
    LoginManager loginManager;
    LinearLayout linearLayout;
    ViewStub viewStub;
    boolean is_email_verified;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);

        toolbar = findViewById(R.id.toolbar);
        emailVerificationTextView = findViewById(R.id.emailVerifyTextView);
        linearLayout = findViewById(R.id.linearLayout);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        accountSecurityViewModel = ViewModelProviders.of(this).get(AccountSecurityViewModel.class);
        loginManager = new LoginManager(this);

        is_email_verified = loginManager.is_email_verified();

        setNavigationIcon();

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);

        emailVerificationTextView.setOnClickListener(debouncedOnClickListener);

        TextView txtChangePassword = findViewById(R.id.changePassTextView);
        txtChangePassword.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.account_security_frame_layout,
                        new ChangePasswordFragment()
                ).commit();
            }
        });

    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.emailVerifyTextView) {
                String email = loginManager.getEmailid();
                emailVerify(email);
            }
        }
    };

    private void emailVerify(String email) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View bottomSheet = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_email, findViewById(R.id.bottomSheet));
        TextView emailTextView = bottomSheet.findViewById(R.id.emailTextView);
        emailTextView.setText(email);
        bottomSheet.findViewById(R.id.sendOtp).setOnClickListener(v1 -> {
            if (!email.equals("NA")) {
                if (!is_email_verified) {
                    if (NetworkCheck.isConnect(this)) {
                        EmailVerificationModel emailVerificationModel = new EmailVerificationModel(email);
                        accountSecurityViewModel.verifyEmailApiCall(emailVerificationModel, getApplication(), this);
                    } else {
                        bottomSheetDialog.dismiss();
                        viewStub.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                    }
                } else {
                    ShowToast.showToast(this, "Email Already Verified");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetDialog.dismiss();
                        }
                    }, 1000);
                }
            } else {
                Toast.makeText(this, "Create an Account to Verify", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomSheetDialog.dismiss();
                    }
                }, 1000);

            }
        });
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
    }

    private void checkNetworkConnectivity() {
        if (!NetworkCheck.isConnect(this)) {
            viewStub.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        } else {
            viewStub.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }
}