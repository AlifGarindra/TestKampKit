package otto.com.sdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.otto.sdk.shared.AppInfo
import com.otto.sdk.shared.initKoin
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.interfaces.TransactionListener
import com.otto.sdk.shared.models.PostRepository
import com.otto.sdk.shared.models.ProfileRepository
import com.otto.sdk.shared.models.ProfileViewModel
import com.otto.sdk.shared.response.Posts
import com.otto.sdk.shared.response.UserAuth
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

  fun setClientKey(clientKey: String): SDKManager {
    this.clientKey = clientKey
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

  fun setPhoneNumber(phone:String) : SDKManager {
    UserAuth.phoneNumber = phone
    return this@SDKManager
  }

  fun setOutletName(name:String) : SDKManager {
    UserAuth.userAccessToken = name
    return this@SDKManager
  }

  fun setClientToken(token:String) : SDKManager {
    UserAuth.clientToken = token
    return this@SDKManager
  }

  fun setUserAccessToken(token:String) : SDKManager {
    UserAuth.userAccessToken = token
    return this@SDKManager
  }

  fun openActivity(){
    var checker = checkFirstAuthLayer()
    if (checker.size == 0){

    }
  }

  fun openPPOB(context:Context) {
    if(UserAuth.userAccessToken != ""){
      var intent = Intent(mContext,WebViewKt::class.java)
      context.startActivity(intent)
    }else{
      var intent = Intent(mContext,WebViewKt::class.java)
      context.startActivity(intent)
    }
  }

  fun openProduct(){

  }

  private fun checkFirstAuthLayer(): ArrayList<String> {
   var checker : ArrayList<String> = ArrayList()
    if(UserAuth.phoneNumber == ""){
      checker.add("phone")
    }
    if(UserAuth.clientToken == ""){
      checker.add("client-token")
    }
    if(UserAuth.outletName == ""){
      checker.add("outlet-name")
    }
    return checker
  }

  private fun checkSecondAuthLayer(): ArrayList<String> {
    var checker = checkFirstAuthLayer()
    if(UserAuth.userAccessToken == ""){
      checker.add("user-access-token")
    }
    return checker
  }

  fun getUserInfo(){

  }


  fun clearSDKSession(){

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