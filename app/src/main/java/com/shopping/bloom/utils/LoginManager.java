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

    private static String guest_token = "guest_token";

    private static  String is_primary_address_available = "is_primary_address_available";

    private static String primary_address_id = "primary_address_id";

    private static String primary_address = "primary_address";

    private static String is_firebase_token_changed = "is_firebase_token_changed";

    private static String is_notification_on = "is_notification_on";

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

    public void setIs_firebase_token_changed(boolean isChanged){
        editor.putBoolean(is_firebase_token_changed, isChanged).commit();
    }

    public boolean is_firebase_token_changed(){
        return sharedPreferences.getBoolean(is_firebase_token_changed, false);
    }

    public void setIs_notification_on(boolean is_on){
        editor.putBoolean(is_notification_on, is_on).commit();
    }

    public boolean is_notification_on(){
        return sharedPreferences.getBoolean(is_notification_on, true);
    }

    public boolean is_email_verified() {
        return sharedPreferences.getBoolean(is_email_verified, false);
    }

    public void setEmail_verified_at(boolean Is_email_verified) {
        editor.putBoolean(is_email_verified, Is_email_verified).commit();
    }

    public void setFirebase_token(String fb_token){
        editor.putString(firebase_token, fb_token);
    }

    public String getFirebase_token(){
        return sharedPreferences.getString(firebase_token, "NA");
    }

    public void setPrimary_address_id(String id){
        editor.putString(primary_address_id, id).commit();
    }

    public String getPrimary_address_id(){
        return sharedPreferences.getString(primary_address_id, "NA");
    }


    public void setIs_primary_address_available(boolean is_verified){
        editor.putBoolean(is_primary_address_available, is_verified).commit();
    }

    public boolean  getIs_primary_address_available(){
        return sharedPreferences.getBoolean(is_primary_address_available, false);
    }

    public void setPrimaryAddress(String address){
        editor.putString(primary_address, address).commit();
    }

    /*
            Stored in Format
            AddressName,AddressLine,city,Pincode,Contact_No

            eg. Home,Demo Society,Mumbai,400010,1234567890
            all separated by ","
     */
    public String getPrimary_address(){
        return sharedPreferences.getString(primary_address, "NA");
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

          //  return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9ibG9vbWFwcC5pblwvYXBpXC9mcm9udGVuZFwvZ2V0UHJvZHVjdHMiLCJpYXQiOjE2MTg2NTk4MjYsImV4cCI6MTYyMzMyNjg2OCwibmJmIjoxNjIwNzM0ODY4LCJqdGkiOiI5RVR1WWlVODJMT0dUckVlIiwic3ViIjoxLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.VPXgReY9FhDaYNitX6MK5CO2TLwKtF8NWT4Mwy3XKjA";
       return sharedPreferences.getString(token, "NA");
    }

    public void setGuest_token(String token){
        editor.putString(guest_token, token).apply();
    }

    public String getGuest_token(){
        return sharedPreferences.getString(guest_token, "NA");
    }

}