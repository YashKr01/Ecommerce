package com.shopping.bloom.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;

import static com.shopping.bloom.utils.Const.LOGIN_ACTIVITY;

public class SettingsActivity extends AppCompatActivity {

    TextView nameTextView, emailTextView, addressBookTextView,
            accountSecurityTextView, connectTextView, notificationTextView;
    LoginManager loginManager;
    LinearLayout linearLayout;
    Toolbar toolbar;
    Button signOutButton;
    int check = 0;

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
        notificationTextView = findViewById(R.id.setNotificationTextView);

        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(debouncedOnClickListener);
        accountSecurityTextView.setOnClickListener(debouncedOnClickListener);
        connectTextView.setOnClickListener(debouncedOnClickListener);
        addressBookTextView.setOnClickListener(debouncedOnClickListener);
        notificationTextView.setOnClickListener(debouncedOnClickListener);

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

        if(loginManager.is_notification_on()){
            notificationTextView.setText("ON");
            check = 0;
        }else{
            notificationTextView.setText("OFF");
            check = 1;
        }

        TextView txtFeedback = findViewById(R.id.ratingTextView);
        txtFeedback.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                showDialog(SettingsActivity.this);
            }
        });

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
            } else if (v.getId() == R.id.accountSecurityTextView) {
                accountSecurityIntent();
            } else if(v.getId() == R.id.setNotificationTextView){
                setNotification();
            }
        }
    };

    private void setNotification() {
        if(check == 0){
            notificationTextView.setText("OFF");
            loginManager.setIs_notification_on(false);
            check = 1;
        }else{
            notificationTextView.setText("ON");
            loginManager.setIs_notification_on(true);
            check = 0;
        }
    }


    public void accountSecurityIntent() {
        if (loginManager.isLoggedIn()) {
            Toast.makeText(this, "You should be logged in", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACTIVITY);
            }, 1500);
        } else {
            intent(AccountSecurityActivity.class);
        }

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
        if (loginManager.isLoggedIn()) {
            Toast.makeText(this, "You should be logged in", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACTIVITY);
            }, 1500);
        } else {
            intent(MyAddressActivity.class);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_ACTIVITY && resultCode == Activity.RESULT_OK){
            finish();
            startActivity(getIntent());
        }
    }

    // show custom dialog
    private void showDialog(Context context) {

        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.custom_feedback_dialog, null);
        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancel = view.findViewById(R.id.btn_feedback_cancel);
        Button submit = view.findViewById(R.id.btn_feedback_submit);
        EditText feedback = view.findViewById(R.id.txt_feedback);

        submit.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {

                String inputFeedback = feedback.getText().toString().trim();

                if (inputFeedback.isEmpty())
                    Toast.makeText(context, "Feedback is Empty", Toast.LENGTH_SHORT).show();

                else {
                    Toast.makeText(context, "Feedback Submitted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        cancel.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                dialog.dismiss();
            }
        });

    }

}