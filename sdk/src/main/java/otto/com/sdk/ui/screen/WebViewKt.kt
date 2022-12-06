package otto.com.sdk.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
  // var webviewBack : String = JSBridge(this).value
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
      return true
    }

    @Deprecated("Deprecated in Java")
    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
      Log.d("test1234", "shouldOverrideUrlLoading2: ")
      return true
    }

    override fun onLoadResource(view: WebView, url: String) {
    }

    override fun onPageFinished(view: WebView, url: String) {
      view.evaluateJavascript("localStorage.getItem('client_token')",{
        Log.d("test1234", "onPageFinished:$it ")
      })
    }


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      if (url != null) {
        Log.d("test1234", "onPageStarted: $url")
        val userAccessToken = "localStorage.setItem('user_access_token', '${UserAuth.userAccessToken}');"
        val clientToken = "localStorage.setItem('client_token', '${UserAuth.clientToken}');"
        webView.evaluateJavascript(clientToken, null)
        webView.evaluateJavascript(userAccessToken, null)
        // if(url.contains("poc")){
        var posts : Posts = postRepository.fetchFirstPost()
        var status = GeneralStatus
        status.state = "success"
        status.message = ""
        generalListener?.onOpenPPOB(status)
        Log.d("test123", "onPageStarted: $posts")
        // }
      }
    }
  }

  var openUrl =  intent.getStringExtra("urlPPOB")
  if(openUrl !== null){
    webView.loadUrl(openUrl)
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
  // webView.addJavascriptInterface(JSBridge(this),"JSBridge")

//        webView.loadUrl("https://test-communication.netlify.app?phoneNumber=123")





  // webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
  //   // val i = Intent(Intent.ACTION_VIEW)
  //   // i.data = Uri.parse(url)
  //   // startActivity(i)
  //   Log.d("download data",Uri.parse(url).toString() )
  //   val request = DownloadManager.Request(Uri.parse(url))
  //   val cookies = CookieManager.getInstance().getCookie(url)
  //   request.addRequestHeader("Cookie",cookies)
  //   request.addRequestHeader("User-Agent",userAgent)
  //   request.setDescription("Downloading requested file....")
  //   request.setMimeType(mimetype)
  //   request.allowScanningByMediaScanner()
  //   request.setNotificationVisibility(
  //     DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
  //   )
  //   val fileName = URLUtil.guessFileName(url, "contentDescription", mimetype)
  //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
  //   request.setTitle(URLUtil.guessFileName(url, "contentDescription", mimetype));
  //   request.setAllowedOverMetered(true)
  //   request.setAllowedOverRoaming(false)
  //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
  //     request.setRequiresCharging(false)
  //     request.setRequiresDeviceIdle(false)
  //   }
  //   request.setVisibleInDownloadsUi(true)
  //   val dManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
  //   dManager.enqueue(request)
  //
  // })

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


  override fun onBackPressed(){
       webView.evaluateJavascript(
           "nativeBackPressed()",
           {
               Log.d("goback", it)
           })
    // webView.evaluateJavascript(
    //   "window.nativeBackPress()",
    //   {
    //     Log.d("goback", it)
    //   })
//     if(webView.canGoBack()){
//       webView.goBack()
//     }else{
//       finish()
//     }
//     webView.loadUrl("tel:081324")

      // val intent = Intent(Intent.ACTION_VIEW);
      // intent.data = Uri.parse("mailto:garindra.alif@gmail.com")
      // startActivity(intent)

    // Log.d("test1234", webView.url.toString())
    // super.onBackPressed()
    // webView.evaluateJavascript("window.localStorage.getItem('client_token')",{
    //   Log.d("test1234", "back:$it ")
    // })

  }
}