package com.otto.sdk.shared.kampkit.android

import android.app.Application
import otto.com.sdk.SDKManager

class MainApp : Application() {

  override fun onCreate() {
    super.onCreate()
    SDKManager.getInstance(this)
      .clientKey("myClientKey")
      .build()
  }
}

