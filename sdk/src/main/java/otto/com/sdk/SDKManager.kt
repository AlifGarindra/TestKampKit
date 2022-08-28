package otto.com.sdk

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import co.touchlab.kampkit.AppInfo
import co.touchlab.kampkit.initKoin
import co.touchlab.kampkit.models.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

data class Config(val clientKey: String)
class SDKManager private constructor(context: Context) {
  companion object : SingletonHolder<SDKManager, Context>(::SDKManager)

  private var mContext: Context

  init {
    Log.e("SDK MANAGER", "INIT")
    mContext = context
    var appId = "Variable A"
  }

  var x: String = "X"
  fun changeTheX(newX: String) {
    x = newX
  }

  lateinit var clientKey: String
  fun clientKey(clientKey: String): SDKManager {
    this.clientKey = clientKey
    return this@SDKManager
  }

  fun build(): SDKManager {
    initKoin(
      module {
        single<Context> { mContext }
        single<Config> { Config(clientKey) }
        viewModel { ProfileViewModel(get(), get { parametersOf("ProfileViewModel") }) }
        single<SharedPreferences> {
          get<Context>().getSharedPreferences(
            "STARTER_SETTINGS",
            Context.MODE_PRIVATE
          )
        }
        single<AppInfo> { AndroidAppInfo }
        single {
          { Log.i("Startup", "Hello there!") }
        }
      }
    )
    return this@SDKManager
  }
  // fun createMainScreen(): Composable {
  //   val vm = ViewModelProvider.AndroidViewModelFactory(mContext as Application)
  //     .create(ProfileViewModel::class.java)
  // }
}