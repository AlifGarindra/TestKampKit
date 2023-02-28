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
import com.otto.sdk.shared.interfaces.UserInfoListener
import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.UserAuth
import com.otto.sdk.shared.localData.UserInfoStatus
import com.otto.sdk.shared.models.PpobRepository
import com.otto.sdk.shared.response.Meta
import org.koin.dsl.module
import otto.com.sdk.ui.screen.WebViewKt
// import io.sentry.Sentry
import org.koin.android.ext.android.inject
import java.util.UUID

// data class Config(val clientKey: String)
class SDKManager private constructor(context: Context) : AppCompatActivity()  {
  private val ppobRepository : PpobRepository by inject()

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
    var onlyNumber = phone.replace("+","")
    if(onlyNumber.startsWith("62")){
      onlyNumber = onlyNumber.replaceRange(0..1, "0")
    }
    if(onlyNumber != UserAuth.phoneNumber){
      UserAuth.userAccessToken = ""
    }
    UserAuth.phoneNumber = onlyNumber
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

  fun setUserAccessToken(token:String? = null) : SDKManager {
    if(token != null){
      UserAuth.userAccessToken = token
        getUserInfo{
          generalListener?.onUserProfile(it)
        }
    }else{
      UserAuth.userAccessToken = ""
    }
    return this@SDKManager
  }


  // fun trySentry(){
  //   var status = object{
  //     val helloWorld = "hellow"
  //     val helloWorld2 = "hellow"
  //   }
  //   try {
  //     // throw Exception()
  //     val data = 20 / 0
  //   } catch (e: Exception) {
  //     Sentry.captureException(e)
  //   }
  // }

  // fun testingHOC(integer: (Int) -> Unit){
  //   integer(10)
  // }
  //
  // fun getBalancePPOB() : String{
  //   return "10000"
  // }

  // fun getPosts(resp:(Any)->(Unit)){
  //   try{
  //     var test = postRepository.fetchFirstPost()
  //     resp(test)
  //   }catch(e:Exception){
  //     resp(e)
  //   }
  // }

  // @SuppressLint("HardwareIds")
  // fun getDeviceId() : String{
  //   return Settings.Secure.getString( mContext.contentResolver,Settings.Secure.ANDROID_ID).toString()
  // }

  fun useSandbox() : SDKManager{
    Constants.isSandbox = true
    Log.d("testsandbox", "useSandbox:${Constants.environtment.Base_URL}")
    return this@SDKManager
  }


  private fun getUserInfo(onSuccess:(UserInfoStatus)->Unit){
    var xtrace :String = UUID.randomUUID().toString()
    try{
      checkFirstAuthLayer()
      checkSecondAuthLayer()
      networkChecking()
      val nowdate : Long = System.currentTimeMillis() / 1000
      var meta : Meta? = null
      ppobRepository.fetchUserInfo("${nowdate}",UserAuth.userAccessToken,UserAuth.phoneNumber,xtrace,
        onResponse = {
            status,userInfo ->
          if(userInfo.meta!== null){
            meta = userInfo.meta!!
          }
          when(status){
            200 ->{
              if(userInfo.account !== null){
                UserInfoStatus.accountId = userInfo.account!!.account_id!!
                UserInfoStatus.balance = userInfo.account?.balance_amount.toString()
                UserInfoStatus.phoneNumber = userInfo.account!!.mobile_phone_number!!
                onSuccess(UserInfoStatus)
              }
            }
            401->{
              if(meta?.code!= null){
                if(meta?.code == "01"){
                  generalListener?.onUserAccessTokenExpired()
                }
                else{
                  onErrorHandler("http", meta!!.code!!, meta!!.message!!)
                }
              }
            }
            else->{
              onErrorHandler("http",meta!!.code!!,meta!!.message!!)
            }
          }
        })
    }catch(e:Exception){
      onErrorHandler("sdk",e.message.toString(),e.message.toString())
    }
  }

  fun openActivation(context:Context){
    try {
      checkFirstAuthLayer()
      networkChecking()
      UserAuth.userAccessToken = ""
      var intent = Intent(mContext,WebViewKt::class.java)
      // intent.putExtra("type","openActivation")
      context.startActivity(intent)
    }catch (e:Exception){
      onErrorHandler("sdk",e.message.toString(),e.message.toString())
    }
  }

  fun openPpob(context:Context) {
    try {
      checkFirstAuthLayer()
      checkSecondAuthLayer()
      networkChecking()
      var intent = Intent(mContext,WebViewKt::class.java)
      // intent.putExtra("type","openPpob")
      context.startActivity(intent)
    }catch (e:Exception){
      onErrorHandler("sdk",e.message.toString(),e.message.toString())
    }
  }

  fun openProduct(context:Context,product:String){
    try {
      checkFirstAuthLayer()
      checkSecondAuthLayer()
      networkChecking()
      var intent = Intent(mContext,WebViewKt::class.java)
      intent.putExtra("urlPPOB","${product}")
      // intent.putExtra("type","openPpob")
      context.startActivity(intent)
    }catch (e:Exception){
      onErrorHandler("sdk",e.message.toString(),e.message.toString())
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

  private fun onErrorHandler(type:String,code:String,message:String){
    ErrorStatus.type = type
    ErrorStatus.code = code
    ErrorStatus.message = message
    generalListener?.onError(ErrorStatus)
  }

   fun networkChecking(){
      if(!NetworkCheck.checkForInternet(mContext)){
        throw Exception("need_internet_access")
      }
  }

  fun userInfoListener(listener : UserInfoListener){
    getUserInfo {
      listener?.onUserInfo(it)
    }
  }


  fun clearSDKSession(){
    UserAuth.reset()
  }


  fun build(): SDKManager {

    // Sentry.init { options ->
    //   options.dsn = "https://03b330c4b8da43d0801e3afcbc6f3983@o4504072784773120.ingest.sentry.io/4504073112387584"
    //   // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
    //   // We recommend adjusting this value in production.
    //   options.tracesSampleRate = 1.0
    //   // When first trying Sentry it's good to see what the SDK is doing:
    //   options.isDebug = true
    // }

    initKoin(
      module {
        single<Context> { mContext }
        // single<Config> { Config(clientKey) }
        // viewModel { ProfileViewModel(get(), get { parametersOf("ProfileViewModel") }) }
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