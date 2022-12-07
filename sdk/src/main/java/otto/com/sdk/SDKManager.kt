package otto.com.sdk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.otto.sdk.shared.AppInfo
import com.otto.sdk.shared.Constants
import com.otto.sdk.shared.initKoin
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.interfaces.TransactionListener
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.models.PostRepository
import com.otto.sdk.shared.models.ProfileViewModel
import com.otto.sdk.shared.localData.UserAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import otto.com.sdk.ui.screen.WebViewKt
import io.sentry.Sentry
import org.koin.android.ext.android.inject

data class Config(val clientKey: String)
class SDKManager private constructor(context: Context) : AppCompatActivity()  {
  private val postRepository : PostRepository by inject()

  companion object : SingletonHolder<SDKManager, Context>(::SDKManager)

  var generalListener : GeneralListener? = null
  var transactionListener : TransactionListener? = null

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

  fun setPhoneNumber(phone:String) : SDKManager {
    UserAuth.phoneNumber = phone
    return this@SDKManager
  }

  fun setOutletName(name:String) : SDKManager {
    UserAuth.outletName = name
    return this@SDKManager
  }

  fun setClientToken(token:String) : SDKManager {
    UserAuth.clientToken = token
    return this@SDKManager
  }

  fun setUserAccessToken(token:String) : SDKManager {
    UserAuth.userAccessToken = token
    //geUserInfo
    return this@SDKManager
  }

  @JvmName("setTheGeneralListener")
  fun setGeneralListener(listener : GeneralListener){
    this.generalListener = listener
  }

  @JvmName("getTheGeneralListener")
  fun getGeneralListener() : GeneralListener? {
    return this.generalListener;
  }

  @JvmName("setTheTransactionListener")
  fun setTransactionListener(listener : TransactionListener){
    this.transactionListener = listener
  }

  @JvmName("getTheTransactionListener")
  fun getTransactionListener() : TransactionListener? {
    return this.transactionListener;
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

  // fun testingHOC(integer: (Int) -> Unit){
  //   integer(10)
  // }
  //
  // fun getBalancePPOB() : String{
  //   return "10000"
  // }

  fun getPosts(resp:(Any)->(Unit)){
    try{
      var test = postRepository.fetchFirstPost()
      resp(test)
    }catch(e:Exception){
      resp(e)
    }
  }

  // @SuppressLint("HardwareIds")
  // fun getDeviceId() : String{
  //   return Settings.Secure.getString( mContext.contentResolver,Settings.Secure.ANDROID_ID).toString()
  // }


  fun openActivation(context:Context,product:String?){
    try {
      checkFirstAuthLayer()
      var intent = Intent(mContext,WebViewKt::class.java)
      intent.putExtra("urlPPOB",Constants.environment.Menu_URL(UserAuth.phoneNumber))
      context.startActivity(intent)
    }catch (e:Exception){
      GeneralStatus.state = e.message.toString()
      generalListener?.onError(GeneralStatus)
    }
  }

  fun openPpob(context:Context) {
    try {
      checkFirstAuthLayer()
      checkSecondAuthLayer()
      var intent = Intent(mContext,WebViewKt::class.java)
      intent.putExtra("urlPPOB",Constants.environment.Menu_URL(UserAuth.phoneNumber))
      context.startActivity(intent)
    }catch (e:Exception){
      GeneralStatus.state = e.message.toString()
      generalListener?.onError(GeneralStatus)
    }
  }

  fun openProduct(context:Context){
    try {
      checkFirstAuthLayer()
      checkSecondAuthLayer()
      var intent = Intent(mContext,WebViewKt::class.java)
      intent.putExtra("urlPPOB",Constants.environment.Menu_URL(UserAuth.phoneNumber))
      context.startActivity(intent)
    }catch (e:Exception){
      GeneralStatus.state = e.message.toString()
      generalListener?.onError(GeneralStatus)
    }
  }

   private fun checkFirstAuthLayer() {
    if(UserAuth.phoneNumber == ""){
      throw Exception("need_phone_number")
    }
    if(UserAuth.clientToken == ""){
      throw Exception("need_client_token")
    }
  }

  private fun checkSecondAuthLayer(){
    if(UserAuth.userAccessToken == ""){
      throw Exception("need_user_access_token")
    }
  }

  fun getUserInfo(){

  }


  fun clearSDKSession(){
    UserAuth.reset()
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