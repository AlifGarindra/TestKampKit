package otto.com.sdk.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.otto.sdk.shared.Constants
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.localData.UserAuth
import otto.com.sdk.NetworkCheck
import otto.com.sdk.R
import otto.com.sdk.SDKManager
import otto.com.sdk.ui.data.nativeDo

class WebViewKt : AppCompatActivity() {
  private val readStoragePermission = 11
  lateinit var webView : WebView
  // private val postRepository : PostRepository by inject()


  var generalListener : GeneralListener? = SDKManager.getInstance(this).generalListener
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview_kt)
    requestPhonePermissions()
    setUpWebView()
    }

  override fun onDestroy() {
    webView.evaluateJavascript("localStorage.clear()",{
      Log.d("test1234", "onPageFinished:$it ")
    })
    var status = GeneralStatus
    status.state = "destroy"
    status.message = ""
    generalListener?.onClosePPOB(status)
    Log.d("test123", "onPageStarted: $generalListener")
    super.onDestroy()
  }


@SuppressLint("SetJavaScriptEnabled")
fun setUpWebView(){
  webView= findViewById(R.id.webviewkt)
  webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
  var webSettings : WebSettings = webView.settings
  webView.addJavascriptInterface(nativeDo(this,webView), "nativeDo")
  webSettings.javaScriptEnabled = true
  webSettings.setSupportZoom(true)
  webSettings.setAppCachePath(this.cacheDir.absolutePath)
  webSettings.cacheMode = WebSettings.LOAD_DEFAULT
  webSettings.databaseEnabled = true
  webSettings.domStorageEnabled = true
  webSettings.allowContentAccess=true
  webSettings.useWideViewPort = true
  webSettings.loadWithOverviewMode = true


  webView.settings.pluginState = WebSettings.PluginState.ON
  webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
    webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url))
  }
  //Harusnya ambil Context dari punyanya host app

  webView.webViewClient = object : WebViewClient() {


    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
      Log.d("test1234", "shouldOverrideUrlLoading1: ")
      //kalau sudah ada implementasi :tel, :mailto  baru return true ya
      return false
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
      Log.d("test1234", "onReceivedSslError:${error!!} ")
      handler.proceed()
    }
    @Deprecated("Deprecated in Java")
    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
      Log.d("test1234", "shouldOverrideUrlLoading2: ")
      //kalau sudah ada implementasi :tel, :mailto baru return true ya
      return false
    }

    override fun onLoadResource(view: WebView, url: String) {
      if(url.startsWith(Constants.environment.Ppob_Domain)){
        setWebviewLocalStorage(view!!)
      }
      try {
        SDKManager.getInstance(this@WebViewKt).networkChecking()
      }catch (e:Exception){
        ErrorStatus.type = "sdk"
        ErrorStatus.code = e.message.toString()
        ErrorStatus.message = e.message.toString()
        generalListener?.onError(ErrorStatus)
      }
    }

    override fun onPageFinished(view: WebView, url: String) {
      if(url.startsWith(Constants.environment.Ppob_Domain)){
        setWebviewLocalStorage(view!!)
      }
      Log.d("test1234", "onPageFinished: $url")
      // var posts : Posts = postRepository.fetchFirstPost()
      var status = GeneralStatus
      status.state = "success"
      status.message = ""
      generalListener?.onOpenPPOB(status)
      // view.evaluateJavascript("localStorage.getItem('phone_number')",{
      //   Log.d("test1234", "onPageFinished:$it ")
      // })
    }


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      if (url != null) {
        if(url.startsWith(Constants.environment.Ppob_Domain)){
          setWebviewLocalStorage(view!!)
        }
        // Log.d("test123", "onPageStarted: $posts")
      }
    }
  }

  var openUrl =  intent.getStringExtra("urlPPOB")
  if(openUrl !== null){
    webView.loadUrl(Constants.environment.Ppob_Domain+Constants.environment.Ppob_Menu_Slug+'/'+openUrl)
  }else{
    webView.loadUrl(Constants.environment.Ppob_Domain+Constants.environment.Ppob_Menu_Slug)
  }
}


  private fun requestPhonePermissions() {
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        readStoragePermission)
    }

    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
        readStoragePermission)
    }

    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_NOTIFICATION_POLICY)
      != PackageManager.PERMISSION_GRANTED) {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ActivityCompat.requestPermissions(this,
          arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
          readStoragePermission)
      }
    }
  }

  @SuppressLint("HardwareIds")
  fun getDeviceId() : String{
    return Settings.Secure.getString(this.contentResolver,Settings.Secure.ANDROID_ID).toString()
  }

  @JavascriptInterface
  fun setWebviewLocalStorage(view:WebView){
    val setWVStorage = "localStorage.setItem('device_id', '${getDeviceId()}');" +
      "localStorage.setItem('phone_number', '${UserAuth.phoneNumber}');" +
      "localStorage.setItem('outlet_name', '${UserAuth.outletName}');" +
      "localStorage.setItem('client_token', '${UserAuth.clientToken}');" +
      "localStorage.setItem('user_access_token', '${UserAuth.userAccessToken}');"
    // val deviceId = "localStorage.setItem('device_id', '${getDeviceId()}');"
    // val phoneNumber = "localStorage.setItem('phone_number', '${UserAuth.phoneNumber}');"
    // val outletName = "localStorage.setItem('outlet_name', '${UserAuth.outletName}');"
    // val clientToken = "localStorage.setItem('client_token', '${UserAuth.clientToken}');"
    // val userAccessToken = "localStorage.setItem('user_access_token', '${UserAuth.userAccessToken}');"
    //   view.evaluateJavascript(setWVStorage, {
    //     Log.d("test1234", "setWebviewLocalStorage:$it ")
    //   })

    view.evaluateJavascript(setWVStorage, null)

      // webView.evaluateJavascript(phoneNumber,  {
      //   Log.d("test1234", "setWebviewLocalStorage:$it ")
      // })
      // webView.evaluateJavascript(outletName,  {
      //   Log.d("test1234", "setWebviewLocalStorage:$it ")
      // })
      // webView.evaluateJavascript(clientToken,  {
      //   Log.d("test1234", "setWebviewLocalStorage:$it ")
      // })
      // webView.evaluateJavascript(userAccessToken,  {
      //   Log.d("test1234", "setWebviewLocalStorage:$it ")
      // })
  }

  override fun onBackPressed(){
    val script = "nativeBackPressed()"
    if(webView.url?.startsWith(Constants.environment.Ppob_Domain) == true){
      webView.evaluateJavascript(script,null)
    }else{
      if(webView.canGoBack()){
        webView.goBack()
      }else{
        finish()
      }
    }
  }
}