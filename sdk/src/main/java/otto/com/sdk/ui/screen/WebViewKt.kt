package otto.com.sdk.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import otto.com.sdk.R
import otto.com.sdk.ui.data.JSBridge

class WebViewKt : AppCompatActivity() {
  private val readStoragePermission = 11
  var webviewBack : String = JSBridge(this).value
  lateinit var secondWV : WebView
  lateinit var button1 : Button
  var helo = "helo"
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview_kt)
    requestPhonePermissions()
    setUpWebView()
    }

@SuppressLint("SetJavaScriptEnabled")
fun setUpWebView(){
  secondWV= findViewById(R.id.webviewkt)
  // var openUrl =  intent.getStringExtra("openURL")
  secondWV.loadUrl("https://phoenix-imkas.ottodigital.id/sakumas?phoneNumber=0857000002")
  secondWV.addJavascriptInterface(JavaScriptInterface(applicationContext), "Android")
  secondWV.settings.javaScriptEnabled = true
  secondWV.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
    secondWV.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url))
  }
  secondWV.settings.setSupportZoom(true)
  secondWV.settings.setAppCachePath(applicationContext.cacheDir.absolutePath)
  secondWV.settings.cacheMode = WebSettings.LOAD_DEFAULT
  secondWV.settings.databaseEnabled = true
  secondWV.settings.domStorageEnabled = true
  secondWV.settings.useWideViewPort = true
  secondWV.settings.loadWithOverviewMode = true

  secondWV.settings.pluginState = WebSettings.PluginState.ON
  // secondWV.addJavascriptInterface(JSBridge(this),"JSBridge")

//        secondWV.loadUrl("https://test-communication.netlify.app?phoneNumber=123")


  secondWV.webViewClient = object : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

      return if (url.startsWith("tel:") || url.startsWith("mailto:")) {
        view.context.startActivity(
          Intent(Intent.ACTION_VIEW, Uri.parse(url))
        )
        true
      } else {
        view.loadUrl(url)
        true
      }
    }

    override fun onLoadResource(view: WebView, url: String) {
    }

    override fun onPageFinished(view: WebView, url: String) {
      // Log.e("URL onPageFinished", url)
      // progressBar.visibility = View.GONE
    }
  }

  // secondWV.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
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
       secondWV.evaluateJavascript(
           "nativeBackPressed()",
           {
               Log.d("goback", it)
           })


//     if(secondWV.canGoBack()){
//       secondWV.goBack()
//     }else{
//       finish()
//     }

  }
}