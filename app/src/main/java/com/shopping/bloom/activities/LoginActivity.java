package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.shopping.bloom.R;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModel.LoginViewModel;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mobileNoEditText;
    ShowToast showToast;
    LoginViewModel loginViewModel;
    Button button;
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
        setContentView(R.layout.activity_login);

        showToast = new ShowToast(this);

        mobileNoEditText = findViewById(R.id.mobileNoEditText);
        button = findViewById(R.id.signInButton);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        button.setOnClickListener(v -> {
            String mobile_no = mobileNoEditText.getText().toString().trim();
            signIn(mobile_no);
        });

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();

    }

    private void checkNetworkConnectivity() {

        if(!NetworkCheck.isConnect(this)){
            viewStub.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
        }else{
            viewStub.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);

    }

    public void signUpActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void signIn(String mobile_no) {

        if (mobile_no == null || mobile_no.isEmpty()) {
            showToast.showToast("Number is Empty");
        } else if (!numberLength(mobile_no)) {
            showToast.showToast("Number length should be 10");
        } else {
            if(NetworkCheck.isConnect(this)){
                LoginModel loginModel = new LoginModel(mobile_no);
                loginViewModel.makeApiCall(loginModel, getApplication(), this);
            }else{
                viewStub.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }
        }

    }

    private boolean numberLength(String mobile_no) {
        return mobile_no.length() == 10;
    }
}