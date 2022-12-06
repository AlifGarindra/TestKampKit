package otto.com.sdk.ui.data

import android.app.Activity
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class nativeDo(var context : Context,var webview:WebView) : AppCompatActivity() {
  var value : String = ""
  @JavascriptInterface
  fun showMessageInNative(message:String){
    //Received message from webview in native, process data
    Log.d("webview", message)
  }
  @JavascriptInterface
  fun seeWebViewValue(message:String){
    //Received message from webview in native, process data
    Log.d("see", message)
    value = message
  }
  @JavascriptInterface
  fun closeWebview(){
    Log.d("test1234", "logdulu: ")
    (context as Activity).finish()
  }
}