package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.shopping.bloom.R;
import com.shopping.bloom.model.LoginModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.utils.Tools;
import com.shopping.bloom.viewModels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mobileNoEditText;
    LoginViewModel loginViewModel;
    Button button;
    TextView textView, textView2;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    SwipeRefreshLayout swipeRefreshLayout;
    private View parent_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);

        mobileNoEditText = findViewById(R.id.mobileNoEditText);
        button = findViewById(R.id.signInButton);
        viewStub = findViewById(R.id.vsEmptyScreen);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        constraintLayout = findViewById(R.id.constrainLayout);
        textView = findViewById(R.id.loginWithPassTextView);
        textView2 = findViewById(R.id.textView6);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        button.setOnClickListener(debouncedOnClickListener);
        textView.setOnClickListener(debouncedOnClickListener);
        textView2.setOnClickListener(debouncedOnClickListener);

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();

    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if(v.getId() == R.id.signInButton){
                String mobile_no = mobileNoEditText.getText().toString().trim();
                signIn(mobile_no);
            }else if(v.getId() == R.id.loginWithPassTextView){
                loginWithPassActivity();
            }else if(v.getId() == R.id.textView6){
                signUpActivity();
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

    private void signUpActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void signIn(String mobile_no) {

        if (mobile_no == null || mobile_no.isEmpty()) {
            Snackbar.make(parent_view, "Mobile Number is Empty", Snackbar.LENGTH_SHORT).show();
        } else if (!numberLength(mobile_no)) {
            Snackbar.make(parent_view, "Mobile Number length should be 10", Snackbar.LENGTH_SHORT).show();
        } else {
            if (NetworkCheck.isConnect(this)) {
                LoginModel loginModel = new LoginModel(mobile_no);
                loginViewModel.makeApiCall(loginModel, getApplication(), this);
            } else {
                viewStub.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }
        }

    }

    private boolean numberLength(String mobile_no) {
        return mobile_no.length() == 10;
    }

    private void loginWithPassActivity() {
        Intent intent = new Intent(this, LoginWithPassActivity.class);
        startActivity(intent);
    }
}