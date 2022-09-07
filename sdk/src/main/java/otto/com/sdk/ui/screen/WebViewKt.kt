package otto.com.sdk.ui.screen

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import otto.com.sdk.R
import otto.com.sdk.ui.data.JSBridge

class WebViewKt : AppCompatActivity() {
  var webviewBack : String = JSBridge(this).value
  lateinit var secondWV : WebView
  lateinit var button1 : Button
  var helo = "helo"
  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview_kt)
    secondWV= findViewById(R.id.webviewkt)
    var openUrl =  intent.getStringExtra("openURL")
    secondWV.webViewClient = WebViewClient()
//        secondWV.settings.domStorageEnabled = true
    secondWV.settings.javaScriptEnabled = true
    secondWV.settings.loadWithOverviewMode = true
    secondWV.settings.useWideViewPort = true
    secondWV.addJavascriptInterface(JSBridge(this),"JSBridge")
//        secondWV.loadUrl("https://test-communication.netlify.app?phoneNumber=123")
       secondWV.loadUrl("https://phoenix-imkas.ottodigital.id/sakumas?phoneNumber=0857000002")
//        secondWV.loadUrl("https://phoenix-imkas.ottodigital.id/webview/imkas-sdk/")
//     secondWV.loadUrl(openUrl!!)
  }

  override fun onBackPressed(){
//        secondWV.evaluateJavascript(
//            "nativeBackPressed()",
//            {
//                Log.d("goback", it)
//            })
    if(secondWV.canGoBack()){
      secondWV.goBack()
    }else{
      finish()
    }

  }
}