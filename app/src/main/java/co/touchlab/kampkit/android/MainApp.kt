package co.touchlab.kampkit.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import co.touchlab.kampkit.AppInfo
import co.touchlab.kampkit.initKoin
import co.touchlab.kampkit.models.BreedViewModel
import co.touchlab.kampkit.models.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

data class Product(val sku: String, val name: String)

class MainApp : Application() {

  override fun onCreate() {
    super.onCreate()
    initKoin(
      module {
        Product("10001", "indomie")
        single<Context> { this@MainApp }
        viewModel { BreedViewModel(get(), get { parametersOf("BreedViewModel") }) }
        viewModel { ProfileViewModel(get(), get { parametersOf("ProfileViewModel") }) }
        single<SharedPreferences> {
          get<Context>().getSharedPreferences(
            "KAMPSTARTER_SETTINGS",
            Context.MODE_PRIVATE
          )
        }
        single<AppInfo> { AndroidAppInfo }
        single {
          { Log.i("Startup", "Hello from Android/Kotlin!") }
        }
      }
    )
  }
}

object AndroidAppInfo : AppInfo {
  override val appId: String = BuildConfig.APPLICATION_ID
}
