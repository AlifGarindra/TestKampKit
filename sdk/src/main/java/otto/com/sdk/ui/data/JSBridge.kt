package otto.com.sdk.ui.data

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface

class JSBridge(var context : Context) {
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
}