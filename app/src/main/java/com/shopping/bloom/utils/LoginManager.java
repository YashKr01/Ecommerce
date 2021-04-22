package com.shopping.bloom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shopping.bloom.App;

public class LoginManager {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    //  on sign up complete
    //  login_manager1.SetLoginStatus(false);
    //  if(login_manager.isLoggedIn()) logged in
    public static LoginManager Instance;

    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String Preference_name = "login_manager_1";

    private static final String IS_LOGED_IN = "islogin";

    private static String name = "name";

    private static String emailid = "emailid";

    private static String mobilenumber = "phone";

    private static String username = "username";

    private static String password = "password";

    private static String token = "token";

    private static String firebase_token = "fbtoken";

    private static String isTokenChanged = "isTokenChanged";

    private static String is_email_verified = "is_email_verified";


    public static LoginManager getInstance() {

        if (Instance == null) {
            Instance = new LoginManager(App.getContext());
        }

        return Instance;
    }



    public LoginManager(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences(Preference_name, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void removeSharedPreference(){
        editor.clear();
        editor.apply();
    }

    public void SetLoginStatus(boolean isFirstTime) {
        editor.putBoolean(IS_LOGED_IN, isFirstTime);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGED_IN, true);
    }

    public boolean is_email_verified() {
        return sharedPreferences.getBoolean(is_email_verified, false);
    }

    public void setEmail_verified_at(boolean Is_email_verified) {
        editor.putBoolean(is_email_verified, Is_email_verified).commit();
    }
    public void setname(String mname) {
        editor.putString(name, mname).commit();
    }

    public String getname() {
        return sharedPreferences.getString(name, "NA");
    }

    public void setEmailid(String email) {
        editor.putString(emailid, email).commit();
    }

    public String getEmailid() {
        return sharedPreferences.getString(emailid, "NA");

    }

    public void setNumber(String number) {
        editor.putString(mobilenumber, number).commit();
    }

    public String getnumber() {
        return sharedPreferences.getString(mobilenumber, "NA");

    }

    public void setPassword(String pass) {
        editor.putString(password, pass).commit();
    }

    public String getPassword() {
        return sharedPreferences.getString(password, "NA");

    }


    public void settoken(String tok) {
        editor.putString(token, tok).apply();

    }

    public String gettoken() {
        return sharedPreferences.getString(token, "NA");

    }


}