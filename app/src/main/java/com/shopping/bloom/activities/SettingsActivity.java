package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shopping.bloom.R;
import com.shopping.bloom.utils.LoginManager;

public class SettingsActivity extends AppCompatActivity {

    TextView nameTextView, emailTextView;
    LoginManager loginManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loginManager = new LoginManager(this);

        toolbar = findViewById(R.id.toolbar);

        setNavigationIcon();

        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);

        String name = loginManager.getname();
        String email = loginManager.getEmailid();

        nameTextView.setText(name);
        emailTextView.setText(email);

    }

    public void accountSecurityIntent(View view) {
        intent(AccountSecurityActivity.class);
    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            intent(MainActivity.class);
            finish();
        });
    }

    public void connectIntent(View view) {
        intent(ConnectToUsActivity.class);
    }

    private void intent(Class<?> className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }

    public void addressBookIntent(View view) {
        intent(MyAddressActivity.class);
    }

    public void signOut(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out?");
        builder.setMessage("Do You Want To Sign Out?");
        builder.setPositiveButton("Sign Out", (dialog, which) -> {
            loginManager.SetLoginStatus(true);
            loginManager.removeSharedPreference();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}