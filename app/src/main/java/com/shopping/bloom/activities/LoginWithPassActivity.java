package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.shopping.bloom.R;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
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

        toolbar.setNavigationOnClickListener(debouncedOnClickListener);

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();

        loginWithPassViewModel = ViewModelProviders.of(this).get(LoginWithPassViewModel.class);

        signInButton.setOnClickListener(debouncedOnClickListener);
        textView.setOnClickListener(debouncedOnClickListener);
        textView2.setOnClickListener(debouncedOnClickListener);

    }
    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.signInButton){
                signIn();
            }else if(v.getId() == R.id.toolbar){
                onBackPressed();
            }else if(v.getId() == R.id.textView6){
                signUpActivity();
            }else if(v.getId() == R.id.loginWithOtpTextView){
                loginWithOtpActivity();
            }
        }
    };

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
        startActivity(intent);
    }

    private void loginWithOtpActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}