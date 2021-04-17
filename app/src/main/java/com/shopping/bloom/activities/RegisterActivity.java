package com.shopping.bloom.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shopping.bloom.model.RegistrationModel;
import com.shopping.bloom.R;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewmodel.RegisterViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText, numberEditText;
    ShowToast showToast;
    Button button;
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        showToast = new ShowToast(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        numberEditText = findViewById(R.id.numberEditText);

        button = findViewById(R.id.signInButton);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        button.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String number = numberEditText.getText().toString().trim();
            signIn(email, number, password);
        });
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
            RegistrationModel registrationModel = new RegistrationModel("demo",email, number, "abc");
            registerViewModel.makeApiCall(registrationModel, this);

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
}