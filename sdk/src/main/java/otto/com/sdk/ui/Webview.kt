package co.touchlab.kampkit.android.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme

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
fun MyWebview(url: String) {
  var backEnabled = remember { mutableStateOf(false) }
  var webView: WebView? = null
  val mContext = LocalContext.current as Activity
  AndroidView(factory = { context ->
    WebView(context).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
      webViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
          backEnabled.value = view.canGoBack()
          if (view.url!!.contains("java")) {
            mContext.finish()
          }
        }
      }
      loadUrl(url)
      settings.javaScriptEnabled = true
      webView = this
    }
  }, update = {
    Log.d("myurl", it.toString())
    webView = it
  })
  BackHandler(enabled = backEnabled.value) {
    Log.d("myurl", backEnabled.toString())
    webView?.goBack()
  }
}
