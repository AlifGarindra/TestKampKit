package otto.com.sdk.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.otto.sdk.shared.Constants
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.localData.UserAuth
import com.otto.sdk.shared.models.PostRepository
import com.otto.sdk.shared.response.Posts
import org.koin.android.ext.android.inject
import otto.com.sdk.R
import otto.com.sdk.SDKManager
import otto.com.sdk.ui.data.nativeDo

class WebViewKt : AppCompatActivity() {
  private val readStoragePermission = 11
  lateinit var webView : WebView
  private val postRepository : PostRepository by inject()


  var generalListener : GeneralListener? = SDKManager.getInstance(this).getGeneralListener()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview_kt)
    requestPhonePermissions()
    setUpWebView()
    }

  override fun onDestroy() {
    webView.evaluateJavascript("window.localStorage.clear()",{
      Log.d("test1234", "onPageFinished:$it ")
    })
    // var status = GeneralStatus
    // status.state = "destroy"
    // status.message = ""
    // generalListener?.onClosePPOB(status)
    // Log.d("test123", "onPageStarted: $generalListener")
    super.onDestroy()
  }


@SuppressLint("SetJavaScriptEnabled")
fun setUpWebView(){
  webView= findViewById(R.id.webviewkt)
  //Harusnya ambil Context dari punyanya host app
  webView.webViewClient = object : WebViewClient() {

    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
      Log.d("test1234", "shouldOverrideUrlLoading1: ")
      //kalau sudah ada implementasi :tel, :mailto  baru return true ya
      return false
    }

    @Deprecated("Deprecated in Java")
    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
      Log.d("test1234", "shouldOverrideUrlLoading2: ")
      //kalau sudah ada implementasi :tel, :mailto baru return true ya
      return false
    }

    override fun onLoadResource(view: WebView, url: String) {

    }

    override fun onPageFinished(view: WebView, url: String) {
      view.evaluateJavascript("localStorage.getItem('device_id')",{
        Log.d("test1234", "onPageFinished:$it ")
      })
    }


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      if (url != null) {
        Log.d("test1234", "onPageStarted: $url")
        setWebviewLocalStorage()
        var posts : Posts = postRepository.fetchFirstPost()
        var status = GeneralStatus
        status.state = "success"
        status.message = ""
        generalListener?.onOpenPPOB(status)
        Log.d("test123", "onPageStarted: $posts")
      }
    }
  }

  var openUrl =  intent.getStringExtra("urlPPOB")
  if(openUrl !== null){
    webView.loadUrl(Constants.environment.Ppob_Domain+Constants.environment.Ppob_Menu_Slug)
  }

  webView.addJavascriptInterface(nativeDo(this,webView), "nativeDo")
  webView.settings.javaScriptEnabled = true
  webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
    webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url))
  }
  webView.settings.setSupportZoom(true)
  webView.settings.setAppCachePath(this.cacheDir.absolutePath)
  webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
  webView.settings.databaseEnabled = true
  webView.settings.domStorageEnabled = true
  webView.settings.useWideViewPort = true
  webView.settings.loadWithOverviewMode = true

  webView.settings.pluginState = WebSettings.PluginState.ON

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

  fun setWebviewLocalStorage(){
    val deviceId = "localStorage.setItem('device_id', '${getDeviceId()}');"
    val phoneNumber = "localStorage.setItem('phone_number', '${UserAuth.phoneNumber}');"
    val outletName = "localStorage.setItem('outlet_name', '${UserAuth.outletName}');"
    val clientToken = "localStorage.setItem('client_token', '${UserAuth.clientToken}');"
    val userAccessToken = "localStorage.setItem('user_access_token', '${UserAuth.userAccessToken}');"
    webView.evaluateJavascript(deviceId, null)
    webView.evaluateJavascript(phoneNumber, null)
    webView.evaluateJavascript(outletName, null)
    webView.evaluateJavascript(clientToken, null)
    webView.evaluateJavascript(userAccessToken, null)
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