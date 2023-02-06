package com.otto.sdk.shared.kampkit.android

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDex
import com.otto.sdk.shared.Constants
import otto.com.sdk.SDKManager

class MainApp : Application() {

  override fun onCreate() {
    super.onCreate()
    if(Build.VERSION.SDK_INT > 19 ){
      SDKManager.getInstance(this)
        .build()
    }
  }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }
}

