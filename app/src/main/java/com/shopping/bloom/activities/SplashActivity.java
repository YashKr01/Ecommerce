package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.model.SplashData;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.Tools;
import com.shopping.bloom.viewModels.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    SplashViewModel splashViewModel;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginManager = new LoginManager(this);

        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);

        String imei_number = Tools.getDeviceID(this);

        splashViewModel.getSplashDataMutableLiveData().observe(this, splashData -> {
            if(splashData != null){
                loginManager.settoken(splashData.getToken());
                Toast.makeText(this, splashData.getToken(), Toast.LENGTH_SHORT).show();
                gotoMainScreen();
            }
        });
        splashViewModel.makeApiCall(imei_number, getApplication());

    }

    private void gotoMainScreen() {
        //removing intent to login
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}