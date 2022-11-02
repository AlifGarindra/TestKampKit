package otto.com.sdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.otto.sdk.shared.AppInfo
import com.otto.sdk.shared.initKoin
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.models.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import otto.com.sdk.ui.screen.WebViewKt
import io.sentry.Sentry

data class Config(val clientKey: String)
class SDKManager private constructor(context: Context) {
  companion object : SingletonHolder<SDKManager, Context>(::SDKManager)

  var generalListener : GeneralListener? = null

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

  @JvmName("setGeneralListener1")
  fun setGeneralListener(listener : GeneralListener){
    this.generalListener = listener
  }

  fun trySentry(){
    var status = object{
      val helloWorld = "hellow"
      val helloWorld2 = "hellow"
    }
    try {
      // throw Exception()
      val data = 20 / 0
    } catch (e: Exception) {
      Sentry.captureException(e)
    }
  }

  fun getBalancePPOB() : String{
    return "10000"
  }

  @JvmName("getGeneralListener1")
  fun getGeneralListener() : GeneralListener? {
    return this.generalListener;
  }

  fun openPPOB(feature:String?,activity: Activity) {
      var intent = Intent(mContext,WebViewKt::class.java)
      activity.startActivity(intent)
    if(feature != null) {
      intent.putExtra("urlPPOB", feature)
    }
  }

  fun build(): SDKManager {

    Sentry.init { options ->
      options.dsn = "https://03b330c4b8da43d0801e3afcbc6f3983@o4504072784773120.ingest.sentry.io/4504073112387584"
      // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
      // We recommend adjusting this value in production.
      options.tracesSampleRate = 1.0
      // When first trying Sentry it's good to see what the SDK is doing:
      options.isDebug = true
    }

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