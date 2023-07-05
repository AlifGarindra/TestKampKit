package otto.com.sdk.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.otto.sdk.shared.Constants
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.localData.UserAuth
import otto.com.sdk.R
import otto.com.sdk.SDKManager
import otto.com.sdk.ui.data.nativeDo

class WebViewKt : AppCompatActivity() {
  private val readStoragePermission = 11
  lateinit var webView : WebView
  // lateinit var chromeV : String
  // private val postRepository : PostRepository by inject()


  var generalListener : GeneralListener? = SDKManager.getInstance(this).generalListener
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d("testsynchronous", "onCreate: jalan")
    setContentView(R.layout.activity_webview_kt)
    if(UserAuth.clientToken != "" && UserAuth.phoneNumber != ""){
      requestPhonePermissions()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        bluetoothPhonePermissions()
      }
      setUpWebView()
      Log.d("testsynchronous", "onCreate selesai: jalan")
    }else{
      (this as Activity).finish()
    }
    }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    Log.d("testsynchronous", "onNewIntent: jalan ")
    if(UserAuth.clientToken != "" && UserAuth.phoneNumber != ""){
      requestPhonePermissions()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        bluetoothPhonePermissions()
      }
      setUpWebView()
      Log.d("testsynchronous", "onNewIntent selesai: jalan")
    }else{
      (this as Activity).finish()
    }
  }

  override fun onDestroy() {
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
  // var userAgent = webView.settings.userAgentString
  // Log.d("useragent", "setUpWebView:$userAgent ")
  // webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
  var webSettings : WebSettings = webView.settings
  webView.addJavascriptInterface(JavaScriptInterface(this), "Android")
  webView.addJavascriptInterface(nativeDo(this,webView), "nativeDo")
  webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
    Log.d("blob", "setUpWebView: downloader called ")
    webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url))
  }
  webView.webViewClient = object : WebViewClient() {
    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
      Log.d("test1234", "shouldOverrideUrlLoading1: ")
      //kalau sudah ada implementasi :tel, :mailto  baru return true ya
      return false
    }


    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError) {
      Log.e("SSL Error", "Error: ${error.primaryError}")

      view?.context?.let {
        AlertDialog.Builder(it)
          .setTitle("SSL Error")
          .setMessage("There is a problem with the SSL certificate. Do you want to proceed?")
          .setPositiveButton("Proceed") { _, _ ->
            handler.proceed()
          }
          .setNegativeButton("Cancel") { _, _ ->
            handler.cancel()
          }
          .setCancelable(false)
          .show()
      }
    }
    @Deprecated("Deprecated in Java")
    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
      Log.d("test1234", "shouldOverrideUrlLoading2: ")
      //kalau sudah ada implementasi :tel, :mailto baru return true ya
      return false
    }
    // override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
    //
    //   return if (url.startsWith("tel:") || url.startsWith("mailto:")) {
    //     view.context.startActivity(
    //       Intent(Intent.ACTION_VIEW, Uri.parse(url))
    //     )
    //     true
    //   } else {
    //     view.loadUrl(url)
    //     true
    //   }
    // }



    override fun onLoadResource(view: WebView, url: String) {
      // if(url.startsWith(Constants.environtment.Ppob_Domain)){
      //   setWebviewLocalStorage(view!!)
      // }
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
      // if(url.startsWith(Constants.environtment.Ppob_Domain)){
      //   setWebviewLocalStorage(view!!)
      // }
      // Toast.makeText(this@WebViewKt,chromeV, Toast.LENGTH_SHORT).show()
      Log.d("test1234", "onPageFinished: $url")
      // var posts : Posts = postRepository.fetchFirstPost()
      var status = GeneralStatus
      status.state = "success"
      status.message = ""
      generalListener?.onOpenPPOB(status)
    }


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      if (url != null) {
        // if(url.startsWith(Constants.environtment.Ppob_Domain)){
        //   setWebviewLocalStorage(view!!)
        // }
      }
    }
  }
  webSettings.defaultTextEncodingName="utf-8"
  webSettings.javaScriptEnabled = true
  webSettings.setSupportZoom(true)
  webSettings.setAppCachePath(this.cacheDir.absolutePath)
  webSettings.cacheMode = WebSettings.LOAD_DEFAULT
  webSettings.databaseEnabled = true
  webSettings.domStorageEnabled = true
  webSettings.allowContentAccess=true
  webSettings.useWideViewPort = true
  webSettings.loadWithOverviewMode = true
  webSettings.javaScriptCanOpenWindowsAutomatically = true

  webView.settings.pluginState = WebSettings.PluginState.ON
  //Harusnya ambil Context dari punyanya host app

  var headers: MutableMap<String, String> = HashMap()
  headers["PHONE-NUMBER"] = UserAuth.phoneNumber
  headers["DEVICE-ID"] = getDeviceId()
  headers["OUTLET-NAME"] = UserAuth.outletName
  headers["CLIENT-TOKEN"] = UserAuth.clientToken
  headers["USER-ACCESS-TOKEN"] = UserAuth.userAccessToken
  Log.d("testsynchronous", "headers - uat: ${UserAuth.userAccessToken}")
  var openUrl =  intent.getStringExtra("urlPPOB")
  if(openUrl != null){
    webView.loadUrl(Constants.environtment.Ppob_Domain+Constants.environtment.Ppob_Menu_Slug+'/'+openUrl,headers)
  }else{
    // webView.loadUrl("https://phoenix-imkas.ottodigital.id/sakumas?phoneNumber=0895611439571")
    webView.loadUrl(Constants.environtment.Ppob_Domain+Constants.environtment.Ppob_Menu_Slug,headers)
    // webView.loadUrl("https://expired-rsa-ev.ssl.com/?_gl=1*1njn39n*_gcl_au*NDkwOTU1MTMyLjE2ODgzNTk0MjE.")

  }
}


  @RequiresApi(Build.VERSION_CODES.S)
  private fun bluetoothPhonePermissions(){
    val permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
    if (permission2 != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf( Manifest.permission.BLUETOOTH_SCAN,
          Manifest.permission.BLUETOOTH_CONNECT,
          Manifest.permission.BLUETOOTH_PRIVILEGED),
        1
      )
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
    // var setWVStorage:String = "localStorage.setItem('device_id', '${getDeviceId()}');" +
    //     "localStorage.setItem('outlet_name', '${UserAuth.outletName}');" +
        // "localStorage.setItem('phone_number', '${UserAuth.phoneNumber}');" +
        // "localStorage.setItem('client_token', '${UserAuth.clientToken}');" +
        // "localStorage.setItem('user_access_token', '${UserAuth.userAccessToken}');"

    // val deviceId = "localStorage.setItem('device_id', '${getDeviceId()}');"
    // val phoneNumber = "localStorage.setItem('phone_number', '${UserAuth.phoneNumber}');"
    // val outletName = "localStorage.setItem('outlet_name', '${UserAuth.outletName}');"
    // val clientToken = "localStorage.setItem('client_token', '${UserAuth.clientToken}');"
    // val userAccessToken = "localStorage.setItem('user_access_token', '${UserAuth.userAccessToken}');"
    //   view.evaluateJavascript(setWVStorage, {
    //     Log.d("test1234", "setWebviewLocalStorage:$it ")
    //   })

    // view.evaluateJavascript(setWVStorage, null)
  }

  override fun onBackPressed(){
    // val script = "if ( nativeBackPressed !== undefined || nativeBackPressed !== null ){ nativeBackPressed() }else{ nativeDo.closeWebview()}"
    val script = "try{nativeBackPressed()}catch(err){nativeDo.closeWebview()}"
    // val script = "nativeDo.onUserAccessTokenExpired()"
    try {
      SDKManager.getInstance(this@WebViewKt).networkChecking()
      if(webView.url?.startsWith(Constants.environtment.Ppob_Domain) == true){
        webView.evaluateJavascript(script,null)
      }else {
        if (webView.canGoBack()) {
          webView.goBack()
        } else {
          finish()
        }
      }
    }catch (e:Exception){
      ErrorStatus.type = "sdk"
      ErrorStatus.code = e.message.toString()
      ErrorStatus.message = e.message.toString()
      generalListener?.onError(ErrorStatus)
      finish()
    }
  }
}