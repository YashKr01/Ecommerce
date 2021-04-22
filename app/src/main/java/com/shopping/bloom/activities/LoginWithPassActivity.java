package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewStub;

import com.google.android.material.textfield.TextInputEditText;
import com.shopping.bloom.R;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.model.LoginWithEmailPassModel;
import com.shopping.bloom.model.LoginWithNumberPassModel;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewmodel.LoginWithPassViewModel;

public class LoginWithPassActivity extends AppCompatActivity {

    TextInputEditText inputEditText, passwordEditText;
    LoginWithPassViewModel loginWithPassViewModel;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    SwipeRefreshLayout swipeRefreshLayout;
    LoginManager loginManager;

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

        inputEditText = findViewById(R.id.inputEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();

        loginWithPassViewModel = ViewModelProviders.of(this).get(LoginWithPassViewModel.class);
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


    public void signIn(View view) {
        String input = inputEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (input == null || input.isEmpty()) {
            inputEditText.setError("Please enter Proper Number/Email");
            inputEditText.requestFocus();
        } else if (password == null || password.isEmpty()) {
            passwordEditText.setError("Please enter a password");
            passwordEditText.requestFocus();
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
            inputEditText.setError("Please Enter Proper Number/Email");
            inputEditText.requestFocus();
        }
    }

    private boolean verifyEmail(String input) {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }

    public void signUpActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}