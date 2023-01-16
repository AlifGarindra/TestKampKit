package otto.com.sdk.ui.data

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.otto.sdk.shared.interfaces.GeneralListener
import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.GeneralStatus
import org.json.JSONObject
import otto.com.sdk.SDKManager

class nativeDo(var context : Context,var webview:WebView) : AppCompatActivity() {
  var value : String = ""
  var generalListener = SDKManager.getInstance(context).generalListener
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
  fun copyToClipBoard(text:String?){
    Log.d("test1234", "copyToClipBoard:$text ")
    try{
      var clipBoard : ClipboardManager = webview.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      var clip : ClipData = ClipData.newPlainText("fromwebview",text!!)
      clipBoard.setPrimaryClip(clip)
      Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }catch (e:Exception){
      Log.d("test1234", "copyToClipBoard:$e ")
    }
  }

  @JavascriptInterface
  fun closeWebview(){
    Log.d("test1234", "logdulu: ")
    (context as Activity).finish()
  }
  @JavascriptInterface
  fun exitWebview(causeString : String?){
    // Log.d("test1234", "exitWebview:$causeString")
    // var cause : JSONObject = JSONObject(causeString)
    // if(cause.has("http") && cause.has("meta"  )){
      generalListener?.onUserAccessTokenExpired()
    // }
    (webview.context!! as Activity).finish()
  }

  @JavascriptInterface
  fun onUserAccessTokenExpired(){
      generalListener?.onUserAccessTokenExpired()
    (webview.context!! as Activity).finish()
  }

  @JavascriptInterface
  fun onClientTokenExpired() {
    generalListener?.onClientTokenExpired()
    (webview.context!! as Activity).finish()
  }

  @JavascriptInterface
  fun onAuthCode(authCode:String?){
    Log.d("test1234", "onAuthCode:${authCode!!}")
    generalListener?.onAuthCode(authCode!!)
    (webview.context!! as Activity).finish()
  }

  @JavascriptInterface
  fun onError(error: String?){
    ErrorStatus.reset()
    var cause : JSONObject = JSONObject(error)
    ErrorStatus.type = "http"
    try{
      if(cause.has("code")){
        var code : String = cause.getString("code")
        ErrorStatus.code = code
      }
      if(cause.has("message")){
        var message : String = cause.getString("message")
        ErrorStatus.message = message
      }
    }catch (e:Exception){
      Log.d("test1234", "onErrorCatch:${e.cause} ${e.message}")
    }
    generalListener?.onError(ErrorStatus)
  }
}