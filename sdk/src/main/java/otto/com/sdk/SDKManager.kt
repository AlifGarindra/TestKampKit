package otto.com.sdk

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.ComponentActivity
import co.touchlab.kampkit.AppInfo
import co.touchlab.kampkit.initKoin
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kampkit.request.Auth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class SDKManager private constructor(context: Context) {
  companion object : SingletonHolder<SDKManager, Context>(::SDKManager)

  val mContext: Context

  init {
    Log.e("SDK MANAGER", "INIT")
    mContext = context
    initKoin(
      module {
        single<Context> { context }
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
  }

  var x: String = "X"
  fun changeTheX(newX: String) {
    x = newX
  }


  // fun createMainScreen(): Composable {
  //   val vm = ViewModelProvider.AndroidViewModelFactory(mContext as Application)
  //     .create(ProfileViewModel::class.java)
  // }
}