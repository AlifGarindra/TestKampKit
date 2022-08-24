package co.touchlab.kampkit.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import co.touchlab.kampkit.AppInfo
import co.touchlab.kampkit.initKoin
import co.touchlab.kampkit.models.BreedViewModel
import co.touchlab.kampkit.models.ProfileViewModel
import com.otto.sdk.SDKManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class MainApp : Application() {

  override fun onCreate() {
    super.onCreate()
    val sdk = SDKManager.getInstance(this);
    println("INIT ${sdk.x}")
    sdk.changeTheX("YYYY")
  }
}

object AndroidAppInfo : AppInfo {
  override val appId: String = BuildConfig.APPLICATION_ID
}
