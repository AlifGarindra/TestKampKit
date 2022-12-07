package com.otto.sdk.shared.kampkit.android

import android.app.Application
import otto.com.sdk.SDKManager

class MainApp : Application() {

  override fun onCreate() {
    super.onCreate()
    // SDKManager.getInstance(Config(context = this, clientKey = "myClientKey123!"))
    val sdkman = SDKManager.getInstance(this)
      .clientKey("myClientKey")
      .build()
    sdkman.changeTheX("NNNNNN")
  }
}

