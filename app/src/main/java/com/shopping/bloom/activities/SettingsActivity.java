package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Intent intent = new Intent(this, AccountSecurityActivity.class);
        startActivity(intent);
    }

    private void setNavigationIcon() {
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}