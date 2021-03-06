package com.shopping.bloom.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.shopping.bloom.R;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.utils.Tools;
import com.shopping.bloom.viewModels.RegisterViewModel;


public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, numberEditText, nameEditText;
    Button button;
    TextView textView;
    ConstraintLayout constraintLayout;
    ViewStub viewStub;
    RegisterViewModel registerViewModel;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    String number;
    private View parent_view;

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
        textView = findViewById(R.id.termsTextView);
        parent_view = findViewById(android.R.id.content);

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
        });

        button = findViewById(R.id.signInButton);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        registerViewModel.getMutableLiveData().observe(this, registerResponseModel -> {
            if (registerResponseModel != null) {
                String success = registerResponseModel.getSuccess();
                String message = registerResponseModel.getMessage();
                if (success.equals("true")) {
                    ShowToast.showToast(this, message);
                    Intent intent = new Intent(this, OtpActivity.class);
                    intent.putExtra("mobile_no", number);
                    intent.putExtra("activityName", "RegisterActivity");
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    finish();
                } else {
                    ShowToast.showToast(this, message);
                }
            }
        });

        button.setOnClickListener(debouncedOnClickListener);

        textView.setOnClickListener(debouncedOnClickListener);

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);
        checkNetworkConnectivity();
    }

    private void customTabs() {
        String url = "https://www.google.com/";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.signInButton) {
                String imeiNumber = Tools.getDeviceID(getApplicationContext());
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                number = numberEditText.getText().toString().trim();
                signIn(name, email, number, password, imeiNumber);
            } else if (v.getId() == R.id.termsTextView) {
                customTabs();
            }
        }
    };

    private void signIn(String name, String email, String number, String password, String imeiNumber) {
        if (name == null || name.isEmpty()) {
            Snackbar.make(parent_view, "Enter a Name.", Snackbar.LENGTH_SHORT).show();
        } else if (!isEmailValid(email)) {
            Snackbar.make(parent_view, "Enter a valid email address.", Snackbar.LENGTH_SHORT).show();
        } else if (email == null || email.isEmpty()) {
            Snackbar.make(parent_view, "Enter email address.", Snackbar.LENGTH_SHORT).show();
        } else if (password == null || password.isEmpty()) {
            Snackbar.make(parent_view, "Enter password.", Snackbar.LENGTH_SHORT).show();
        } else if (!isPasswordGreaterThan8(password)) {
            Snackbar.make(parent_view, "Password Length should be greater than 8.", Snackbar.LENGTH_SHORT).show();
        } else if (number == null || number.isEmpty()) {
            Snackbar.make(parent_view, "Mobile No. is Empty", Snackbar.LENGTH_SHORT).show();
        } else if (!numberLength(number)) {
            Snackbar.make(parent_view, "Mobile No. length should be 10.", Snackbar.LENGTH_SHORT).show();
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