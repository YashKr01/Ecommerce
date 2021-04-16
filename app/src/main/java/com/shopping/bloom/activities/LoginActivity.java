package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.shopping.bloom.R;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewmodel.LoginViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mobileNoEditText;
    ShowToast showToast;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showToast = new ShowToast(this);

        mobileNoEditText = findViewById(R.id.mobileNoEditText);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

    }

    public void signUpActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        String mobile_no = mobileNoEditText.getText().toString().trim();

        if (mobile_no == null || mobile_no.isEmpty()) {
            showToast.showToast("Number is Empty");
        } else if (!numberLength(mobile_no)) {
            showToast.showToast("Number length should be 10");
        }else{
            loginViewModel.makeApiCall(mobile_no);
//            Intent intent = new Intent(this, OtpActivity.class);
//            intent.putExtra("mobile_no", mobile_no);
//            startActivity(intent);
        }

    }

    private boolean numberLength(String mobile_no) {
        return mobile_no.length() == 10;
    }
}