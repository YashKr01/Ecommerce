package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.shopping.bloom.R;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.LoginWithPassViewModel;

public class LoginWithPassActivity extends AppCompatActivity {

    TextInputEditText inputEditText, passwordEditText;
    LoginWithPassViewModel loginWithPassViewModel;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    TextView textView, textView2;
    Button signInButton;
    SwipeRefreshLayout swipeRefreshLayout;
    LoginManager loginManager;
    Toolbar toolbar;
    private View parent_view;

    @Override
    protected void onStart() {
        super.onStart();
        loginManager = new LoginManager(this);
        if (!loginManager.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_pass);

        parent_view = findViewById(android.R.id.content);

        inputEditText = findViewById(R.id.inputEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);
        toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.loginWithOtpTextView);
        textView2 = findViewById(R.id.textView6);
        signInButton = findViewById(R.id.signInButton);

        toolbar.setNavigationOnClickListener(v -> {
            loginWithOtpActivity();
        });

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();

        loginWithPassViewModel = ViewModelProviders.of(this).get(LoginWithPassViewModel.class);

        loginWithPassViewModel.getMobileMutableLiveData().observe(this, loginWithPassResponseModel -> {
            if (loginWithPassResponseModel != null) {
                String success = loginWithPassResponseModel.getSuccess();
                String message = loginWithPassResponseModel.getMessage();

                if (success.equals("true")) {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                    loginManager.setEmailid(loginWithPassResponseModel.getData().getUserInfo().getEmail());
                    loginManager.setname(loginWithPassResponseModel.getData().getUserInfo().getName());
                    loginManager.setNumber(loginWithPassResponseModel.getData().getUserInfo().getMobile_no());
                    loginManager.setFirebase_token(loginWithPassResponseModel.getData().getUserInfo().getFirebase_token());
                    loginManager.settoken(loginWithPassResponseModel.getData().getToken());
                    loginManager.SetLoginStatus(false);
                    String email_verified_at = loginWithPassResponseModel.getData().getUserInfo().getEmail_verified_at();
                    if (email_verified_at == null || email_verified_at.isEmpty() || email_verified_at.equals("")) {
                        System.out.println("EmailVerified Empty");
                    } else {
                        loginManager.setEmail_verified_at(true);
                    }

                    Intent resultIntent = getIntent();

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                } else {
                    ShowToast.showToast(this, message);
                }
            } else {
                ShowToast.showToast(this, "Failed");
            }
        });

        loginWithPassViewModel.getEmailModelMutableLiveData().observe(this, loginWithPassResponseModel -> {
            if (loginWithPassResponseModel != null) {
                String success = loginWithPassResponseModel.getSuccess();
                String message = loginWithPassResponseModel.getMessage();

                if (success.equals("true")) {
                    ShowToast.showToast(this, message);

                    loginManager.setEmailid(loginWithPassResponseModel.getData().getUserInfo().getEmail());
                    loginManager.setname(loginWithPassResponseModel.getData().getUserInfo().getName());
                    loginManager.setNumber(loginWithPassResponseModel.getData().getUserInfo().getMobile_no());
                    loginManager.setFirebase_token(loginWithPassResponseModel.getData().getUserInfo().getFirebase_token());
                    loginManager.settoken(loginWithPassResponseModel.getData().getToken());
                    loginManager.SetLoginStatus(false);
                    String email_verified_at = loginWithPassResponseModel.getData().getUserInfo().getEmail_verified_at();
                    if (email_verified_at != null || !email_verified_at.isEmpty() || !email_verified_at.equals("")) {
                        loginManager.setEmail_verified_at(true);
                    }

                    Intent resultIntent = getIntent();

                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    ShowToast.showToast(this, message);
                }
            }
        });

        signInButton.setOnClickListener(debouncedOnClickListener);
        textView.setOnClickListener(debouncedOnClickListener);
        textView2.setOnClickListener(debouncedOnClickListener);

    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.signInButton) {
                if (checkNetworkConnectivity()) {
                    signIn();
                }
            } else if (v.getId() == R.id.textView6) {
                signUpActivity();

            } else if (v.getId() == R.id.loginWithOtpTextView) {
                loginWithOtpActivity();
            }
        }
    };

    private boolean checkNetworkConnectivity() {
        swipeRefreshLayout.setRefreshing(false);
        if (!NetworkCheck.isConnect(this)) {
            viewStub.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            return false;
        } else {
            viewStub.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private void signIn() {
        String input = inputEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (input == null || input.isEmpty()) {
            Snackbar.make(parent_view, "Please enter Proper Number/Email", Snackbar.LENGTH_SHORT).show();
        } else if (password == null || password.isEmpty()) {
            Snackbar.make(parent_view, "Please enter a password", Snackbar.LENGTH_SHORT).show();
        } else {
            signInWithPass(input, password);
        }
    }

    private void signInWithPass(String input, String password) {
        if (input.length() == 10 && !verifyEmail(input)) {
            if (NetworkCheck.isConnect(this)) {
                LoginWithNumberPassModel loginModel = new LoginWithNumberPassModel(input, password);
                loginWithPassViewModel.makeApiCallWithNumber(loginModel, getApplication(), this);
            } else {
                viewStub.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }
        } else if (verifyEmail(input)) {
            if (NetworkCheck.isConnect(this)) {
                LoginWithEmailPassModel loginModel = new LoginWithEmailPassModel(input, password);
                loginWithPassViewModel.makeApiCallWithEmail(loginModel, getApplication(), this);
            } else {
                viewStub.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }
        } else {
            Snackbar.make(parent_view, "Please Enter Proper Number/Email", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean verifyEmail(String input) {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    public void signUpActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    private void loginWithOtpActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }
}