package com.shopping.bloom.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewmodel.RegisterViewModel;


public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, numberEditText;
    ShowToast showToast;
    Button button;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    RegisterViewModel registerViewModel;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        showToast = new ShowToast(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        numberEditText = findViewById(R.id.numberEditText);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);

        button = findViewById(R.id.signInButton);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        button.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String number = numberEditText.getText().toString().trim();
            signIn(email, number, password);
        });

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();
    }

    public void signIn(String email, String number, String password) {

        if (email == null || email.isEmpty()) {
            showToast.showToast("Enter email address.");
        }
        else if (!isEmailValid(email)) {
            showToast.showToast("Enter a valid email address.");
        }
        else if (password== null || password.isEmpty()) {
            showToast.showToast("Enter password.");
        }

        else if (!isPasswordGreaterThan8(password)) {
            showToast.showToast("Password Length should be greater than 8.");
        }

        else if (number == null || number.isEmpty()) {
            showToast.showToast("Number is Empty");
        }

        else if (!numberLength(number)) {
            showToast.showToast("Number length should be 10");
        }
        else{
            if(NetworkCheck.isConnect(this)){
                RegistrationModel registrationModel = new RegistrationModel("demo",email, number, "abc");
                registerViewModel.makeApiCall(registrationModel, getApplication(), this);
            }else{
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

        if(!NetworkCheck.isConnect(this)){
            viewStub.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
        }else{
            viewStub.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);

    }
}