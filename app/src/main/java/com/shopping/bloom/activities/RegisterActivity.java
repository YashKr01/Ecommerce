package com.shopping.bloom.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.utils.Tools;
import com.shopping.bloom.viewModels.RegisterViewModel;


public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, numberEditText, nameEditText;
    Button button;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    RegisterViewModel registerViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        numberEditText = findViewById(R.id.numberEditText);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        button = findViewById(R.id.signInButton);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        button.setOnClickListener(v -> {
            String imeiNumber = Tools.getDeviceID(this);
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String number = numberEditText.getText().toString().trim();
            signIn(name, email, number, password, imeiNumber);
        });

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();
    }

    public void signIn(String name, String email, String number, String password, String imeiNumber) {

        if (email == null || email.isEmpty()) {
            ShowToast.showToast(this, "Enter email address.");
        } else if (!isEmailValid(email)) {
            ShowToast.showToast(this, "Enter a valid email address.");
        } else if (name == null || name.isEmpty()) {
            ShowToast.showToast(this, "Enter Name");
        } else if (password == null || password.isEmpty()) {
            ShowToast.showToast(this, "Enter password.");
        } else if (!isPasswordGreaterThan8(password)) {
            ShowToast.showToast(this, "Password Length should be greater than 8.");
        } else if (number == null || number.isEmpty()) {
            ShowToast.showToast(this, "Mobile No. is Empty");
        } else if (!numberLength(number)) {
            ShowToast.showToast(this,"Mobile No. length should be 10");
        } else {
            if (NetworkCheck.isConnect(this)) {
                RegistrationModel registrationModel = new RegistrationModel(name, email, number, password, "abc", imeiNumber, "android");
                registerViewModel.makeApiCall(registrationModel, getApplication(), this);
            } else {
                viewStub.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordGreaterThan8(String password) {
        return password.length() > 7;
    }

    private boolean numberLength(String number) {
        return number.length() == 10;
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