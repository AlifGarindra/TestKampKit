package co.touchlab.kampkit.android

import android.app.Application
import otto.com.sdk.SDKManager

class MainApp : Application() {

  override fun onCreate() {
    super.onCreate()
    val sdk = SDKManager.getInstance(this)
    println("INIT ${sdk.x}")
    sdk.changeTheX("YYYY")
  }
}

