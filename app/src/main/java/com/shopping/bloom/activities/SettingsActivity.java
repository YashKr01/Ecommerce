package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;

public class SettingsActivity extends AppCompatActivity {

    TextView nameTextView, emailTextView, addressBookTextView, accountSecurityTextView, connectTextView;
    LoginManager loginManager;
    LinearLayout linearLayout;
    Toolbar toolbar;
    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loginManager = new LoginManager(this);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            setNavigationOnClick();
        });
        linearLayout = findViewById(R.id.idLinearLayout);

        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        addressBookTextView = findViewById(R.id.addressBookTextView);
        accountSecurityTextView = findViewById(R.id.accountSecurityTextView);
        connectTextView = findViewById(R.id.connectTextView);

        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(debouncedOnClickListener);
        addressBookTextView.setOnClickListener(debouncedOnClickListener);
        accountSecurityTextView.setOnClickListener(debouncedOnClickListener);
        connectTextView.setOnClickListener(debouncedOnClickListener);

        String name = loginManager.getname();
        String email = loginManager.getEmailid();

        if (name.equals("NA") || email.equals("NA")) {
            linearLayout.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            signOutButton.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            nameTextView.setText(name);
            emailTextView.setText(email);
        }

    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.signOutButton) {
                signOut();
            } else if (v.getId() == R.id.addressBookTextView) {
                addressBookIntent();
            } else if (v.getId() == R.id.connectTextView) {
                connectIntent();
            }else if(v.getId() == R.id.accountSecurityTextView){
                accountSecurityIntent();
            }
        }
    };


    public void accountSecurityIntent() {
        intent(AccountSecurityActivity.class);
    }

    private void setNavigationOnClick() {
        intent(MainActivity.class);
        finish();
    }

    public void connectIntent() {
        intent(ConnectToUsActivity.class);
    }

    private void intent(Class<?> className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }

    public void addressBookIntent() {
        intent(MyAddressActivity.class);
    }

    public void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out?");
        builder.setMessage("Do You Want To Sign Out?");
        builder.setPositiveButton("Sign Out", (dialog, which) -> {
            loginManager.SetLoginStatus(true);
            loginManager.removeSharedPreference();
            intent(MainActivity.class);
            finish();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

}