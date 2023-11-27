package com.melfouly.bestbuycopycat.presentation

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        if(FacebookSdk.isInitialized()) {
            AppEventsLogger.activateApp(this)
        } else {
            FacebookSdk.sdkInitialize(this)
            AppEventsLogger.activateApp(this)
        }

        FacebookSdk.setAutoLogAppEventsEnabled(false)
    }
}