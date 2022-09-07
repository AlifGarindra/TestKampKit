package otto.com.sdk.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.otto.sdk.shared.kampkit.android.ui.theme.KaMPKitTheme
import otto.com.sdk.ui.data.JSBridge

class Webview : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      KaMPKitTheme {
        // A surface container using the 'background' color from the theme
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          var myUrl = intent.getStringExtra("openURL").toString()
          Log.d("myurl", myUrl)
          MyWebview(myUrl)
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d("finish", "onDestroy: ")
  }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyWebview(url : String) {
  var backEnabled =  remember { mutableStateOf(false) }
  var webView by remember { mutableStateOf<WebView?>(null) }
  val mContext = LocalContext.current as Activity

  Column() {
    AndroidView(factory = {
        context->
      WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
        )
        webViewClient = object : WebViewClient(){
          override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
            backEnabled.value = view.canGoBack()
            if(view.url!!.contains("java")){
              mContext.finish()
            }
          }
        }
        loadUrl("https://test-communication.netlify.app?phoneNumber=123")
        settings.javaScriptEnabled = true
        addJavascriptInterface(JSBridge(mContext),"JSBridge")


      }.also {  webView = it }
    }, update = {
      Log.d("myurl", it.toString())
      webView = it
    })
    Button(onClick = {
      (webView?.post{
        webView!!.evaluateJavascript("javascript: "+"updateFromNative(${"halo"})", ValueCallback {
          Log.d("webview", webView!!.url.toString())
        })
        webView!!.loadUrl("javascript: "+"updateFromNative(${"halo"})")
      } ?: null) }) {
      Text(text = "halo")
    }

  }
  BackHandler(enabled = backEnabled.value) {
    Log.d("myurl", backEnabled.toString())
    webView?.goBack()
  }
}
