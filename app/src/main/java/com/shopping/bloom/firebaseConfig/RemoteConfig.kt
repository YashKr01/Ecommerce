package com.shopping.bloom.firebaseConfig

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson

import com.shopping.bloom.BuildConfig
import com.shopping.bloom.firebaseConfig.RemoteConfigDefaults.Companion.MAINSCREEN_CONFIG
import com.shopping.bloom.models.MainScreenConfig
import com.shopping.bloom.utils.CommonUtils

open class RemoteConfig {


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null

        @JvmStatic
        fun getInstance(context: Context): FirebaseRemoteConfig {
            if (mFirebaseRemoteConfig == null) {
                FirebaseApp.initializeApp(context)
                mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
                val configSettings = FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(10)
                    .build()

                if (BuildConfig.DEBUG) {
                    mFirebaseRemoteConfig?.setConfigSettingsAsync(configSettings)
                }
                mFirebaseRemoteConfig?.setDefaultsAsync(RemoteConfigDefaults.defaultMap)

                mFirebaseRemoteConfig?.fetchAndActivate()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("Config sync successful", "")
                        } else {
                            print("Config sync failed")

                        }
                    }

            }
            return mFirebaseRemoteConfig!!
        }


       @JvmStatic
        fun getMainScreenConfig(context: Context): MainScreenConfig {
            var json = getInstance(context).getString(RemoteConfigDefaults.MAINSCREEN_CONFIG.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.MAINSCREEN_CONFIG.value().toString()
            }
            return Gson().fromJson(json, MainScreenConfig::class.java)
        }

      /*  @JvmStatic
        fun getAboutConfig(context: Context): AboutConfig {
            var json = getInstance(context).getString(RemoteConfigDefaults.ABOUT_CONFIG.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.ABOUT_CONFIG.value().toString()
            }
            return Gson().fromJson(json, AboutConfig::class.java)
        }

        @JvmStatic
        fun getLiveMsgConfig(context: Context): LiveMessageConfig {
            var json = getInstance(context).getString(RemoteConfigDefaults.LIVE_MSG_CONFIG.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.LIVE_MSG_CONFIG.value().toString()
            }
            return Gson().fromJson(json, LiveMessageConfig::class.java)
        }

        @JvmStatic
        fun getMemberShipConfig(context: Context): MembershipConfig {
            var json = getInstance(context).getString(RemoteConfigDefaults.MEMBERSHIP_CONFIG.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.MEMBERSHIP_CONFIG.value().toString()
            }
            return Gson().fromJson(json, MembershipConfig::class.java)
        }
        @JvmStatic
        fun getPaymentConfig(context: Context): PaymentConfig {
            var json = getInstance(context).getString(RemoteConfigDefaults.PAYMENT_CONFIG.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.PAYMENT_CONFIG.value().toString()
            }
            return Gson().fromJson(json, PaymentConfig::class.java)
        }
        @JvmStatic
        fun getPaymentConfigNew(context: Context): PaymentConfigNew {
            var json = getInstance(context).getString(RemoteConfigDefaults.PAYMENT_CONFIG_NEW.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.PAYMENT_CONFIG_NEW.value().toString()
            }
            return Gson().fromJson(json, PaymentConfigNew::class.java)
        }
        @JvmStatic
        fun getCommonConfig(context: Context): CommonConfig {
            var json = getInstance(context).getString(RemoteConfigDefaults.COMMON_CONFIG.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.COMMON_CONFIG.value().toString()
            }
            return Gson().fromJson(json, CommonConfig::class.java)
        }
        @JvmStatic
        fun getChatLimitConfig(context: Context): ChatLimitConfig {
            var json = getInstance(context).getString(RemoteConfigDefaults.CHAT_LIMIT_CONFIG.key())
            if (CommonUtils.isNull(json)) {
                json = RemoteConfigDefaults.CHAT_LIMIT_CONFIG.value().toString()
            }
            return Gson().fromJson(json, ChatLimitConfig::class.java)
        }*/
    }
}
