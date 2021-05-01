package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.model.SplashData;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.Tools;
import com.shopping.bloom.viewModels.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    SplashViewModel splashViewModel;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkNetworkConnectivity();

    }

    private void gotoMainScreen() {
        //removing intent to login
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void checkNetworkConnectivity() {

        if (!NetworkCheck.isConnect(this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("No Internet");
            builder.setPositiveButton("Retry", (dialog, which) -> {
                checkNetworkConnectivity();
            });
            builder.setNegativeButton("Close", (dialog, which) -> {
                finish();
            });
            builder.show();

        } else {

            loginManager = new LoginManager(this);

            splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);


            String imei_number = Tools.getDeviceID(this);

            if (loginManager.isLoggedIn()) {

                splashViewModel.getSplashDataMutableLiveData().observe(this, splashData -> {
                    if (splashData != null) {
                        loginManager.setGuest_token(splashData.getToken());

                        gotoMainScreen();
                    }
                });

            } else {
                splashViewModel.getRefreshTokenResponseMutableLiveData().observe(this, refreshTokenResponse -> {
                    if (refreshTokenResponse != null) {
                        if (!refreshTokenResponse.isSuccess()) {
                            loginManager.settoken(refreshTokenResponse.getData().getToken());
                        }
                        gotoMainScreen();
                    }
                });

            }

            if (loginManager.isLoggedIn()) {
                splashViewModel.makeApiCall(imei_number, getApplication());

            } else {
                splashViewModel.makeApiCallCheckToken(loginManager.gettoken(), getApplication());
            }
        }

    }
}