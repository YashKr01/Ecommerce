package com.shopping.bloom;

import android.app.Application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;


public class App extends Application {

    private static final String TAG = "App";
    private static Context context;

    private static Context mContext = null;
    // private static MesiboCall mCall = null;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        //   Fabric.with(this, new Crashlytics());

        mContext = getApplicationContext();
        // throw new RuntimeException("This is a crash");
      /*  MultiDex.install(this);
        FirebaseApp.initializeApp(getApplicationContext());


        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
*/

        Log.e(TAG, TAG);
    }



    public static Context getContext() {
        return mContext;
    }



}