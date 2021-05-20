package com.shopping.bloom.activities;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.shopping.bloom.R;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;

import static com.shopping.bloom.utils.Const.LOGIN_ACTIVITY;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getName();

    TextView nameTextView, emailTextView, addressBookTextView,
            accountSecurityTextView, connectTextView;
    SwitchCompat swToggleNotification;
    LinearLayout linearLayout;
    Toolbar toolbar;
    TextView signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        setUpNameAndMail();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        linearLayout = findViewById(R.id.idLinearLayout);

        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        addressBookTextView = findViewById(R.id.addressBookTextView);
        accountSecurityTextView = findViewById(R.id.accountSecurityTextView);
        connectTextView = findViewById(R.id.connectTextView);
        //notificationTextView = findViewById(R.id.setNotificationTextView);
        swToggleNotification = findViewById(R.id.switchNotification);
        signOutButton = findViewById(R.id.signOutButton);


        //Attach click listener
        signOutButton.setOnClickListener(debouncedOnClickListener);
        accountSecurityTextView.setOnClickListener(debouncedOnClickListener);
        connectTextView.setOnClickListener(debouncedOnClickListener);
        addressBookTextView.setOnClickListener(debouncedOnClickListener);

        swToggleNotification.setOnClickListener(view -> {
            boolean isChecked = swToggleNotification.isChecked();
            setNotification(isChecked);
        });

        findViewById(R.id.ratingTextView).setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                showFeedbackDialog(SettingsActivity.this);
            }
        });

    }

    private void setUpNameAndMail() {
        LoginManager loginManager = LoginManager.getInstance();
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

        swToggleNotification.setChecked(loginManager.is_notification_on());

    }

    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.signOutButton) {
                showSignOutDialog();
            } else if (v.getId() == R.id.addressBookTextView) {
                openAddressBook();
            } else if (v.getId() == R.id.connectTextView) {
                openContactUsActivity();
            } else if (v.getId() == R.id.accountSecurityTextView) {
                openAccountSecurityActivity();
            }
        }
    };

    private void setNotification(boolean notificationState) {
        LoginManager loginManager = new LoginManager(this);
        loginManager.setIs_notification_on(notificationState);
    }


    public void openAccountSecurityActivity() {
        LoginManager loginManager = LoginManager.getInstance();
        if (loginManager.isLoggedIn()) {
            Toast.makeText(this, "Log in Required", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACTIVITY);
            }, 1500);
        } else {
            gotoScreen(AccountSecurityActivity.class);
        }
    }

    public void openContactUsActivity() {
        gotoScreen(ConnectToUsActivity.class);
    }

    private void gotoScreen(Class<?> className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }

    public void openAddressBook() {
        LoginManager loginManager = LoginManager.getInstance();
        if (loginManager.isLoggedIn()) {
            Toast.makeText(this, "You should be logged in", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACTIVITY);
            }, 1500);
        } else {
            gotoScreen(MyAddressActivity.class);
        }
    }

    public void showSignOutDialog() {
        LoginManager loginManager = LoginManager.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign out?");
        builder.setMessage("Do You Want To Sign out?");
        builder.setPositiveButton("Sign out", (dialog, which) -> {
            loginManager.SetLoginStatus(true);
            loginManager.removeSharedPreference();
            gotoScreen(MainActivity.class);
            finish();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_ACTIVITY && resultCode == Activity.RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }

    // show custom dialog
    private void showFeedbackDialog(Context context) {

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