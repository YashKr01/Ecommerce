package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.shopping.bloom.R;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewmodel.LoginViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        button.setOnClickListener(v -> {
            String mobile_no = mobileNoEditText.getText().toString().trim();
            signIn(mobile_no);
        });

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
            LoginModel loginModel = new LoginModel(mobile_no);
            loginViewModel.makeApiCall(loginModel, getApplication(), this);
        }

    }

    private boolean numberLength(String mobile_no) {
        return mobile_no.length() == 10;
    }
}