package otto.com.sdk.ui.data

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.anggastudio.printama.Printama
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.UserAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import otto.com.sdk.R
import otto.com.sdk.SDKManager
import otto.com.sdk.static.userTokenTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// aall functions to provide a communication between webview javascript and native android function

class nativeDo(var context : Context,var webview:WebView) : AppCompatActivity() {
  var value : String = ""
  var generalListener = SDKManager.getInstance(context).generalListener
  // val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

private fun checkBluetooth(){
  val REQUEST_ENABLE_BT = 1 // Any integer value

// Create a BluetoothAdapter object
  val bluetoothManager = webview.context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

// Get a reference to the BluetoothAdapter
  val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

// Check if Bluetooth is supported on the device
  if (bluetoothAdapter == null) {
    // Bluetooth is not supported on this device
    // Handle this case accordingly
  } else {
    // Check if Bluetooth is enabled on the device
    if (!bluetoothAdapter.isEnabled) {
      // Bluetooth is not enabled
      // Ask the user to turn it on
      val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
      (webview.context!! as Activity).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    } else {
      // Bluetooth is already enabled
      // Do whatever you want to do with Bluetooth
    }
  }
}

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
    Log.d("test1234", "close webview triggered")
    (context as Activity).finish()
  }

  @JavascriptInterface
  fun onUserAccessTokenExpired(){
    var counter = userTokenTask.failCounter
    var timestamp = userTokenTask.failTimeStamp
    val nowdate : Long = System.currentTimeMillis() / 1000
    Log.d("testsynchronous", "onUserAccessTokenExpired-fromwv - failtimestamp -  ${timestamp}")
    Log.d("testsynchronous", "onUserAccessTokenExpired-fromwv - failcounter -  ${counter}")

    if(timestamp != null && timestamp <= nowdate){
      userTokenTask.failCounter = 5
      userTokenTask.inProgress = false
      userTokenTask.failTimeStamp = null
    }else{
      if(counter == 0){
        userTokenTask.failCounter = 5
        userTokenTask.inProgress = false
        userTokenTask.failTimeStamp = null
      }
    }
    if(userTokenTask.inProgress && userTokenTask.failCounter != 5){
      Log.d("testsynchronous", "onUserAccessTokenExpired-fromwv - inprogress and failcounter != 5")
      userTokenTask.failCounter = userTokenTask.failCounter - 1
    }else{
      Log.d("testsynchronous", "onUserAccessTokenExpired-fromwv - running")
      userTokenTask.failCounter = userTokenTask.failCounter - 1
      userTokenTask.inProgress = true
      userTokenTask.failTimeStamp = nowdate + 20
      generalListener?.onUserAccessTokenExpired()
    }
    (webview.context!! as Activity).finish()
  }

  @JavascriptInterface
  fun onClientTokenExpired() {
    generalListener?.onClientTokenExpired()
    (webview.context!! as Activity).finish()
  }

  @RequiresApi(Build.VERSION_CODES.M)
  @JavascriptInterface
  fun onPrintHistory(data:String?,hargaJual:String? = "0"){
    checkBluetooth()
    var jElm: JsonElement = JsonParser.parseString(data)
    var jObj : JsonObject = jElm.asJsonObject
    var trans = jObj.get("transaction").asJsonObject
    var refID = trans.get("wallet_reference_number").asString
    var transArray = jObj.getAsJsonArray("transaction_receipt_new")
    var locale = jObj.getAsJsonObject("locale")
    var translist = transArray.asList()
    Log.d("test1234", "${jElm}")
    val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    val currentTime = Calendar.getInstance().time
    val formattedTime = dateFormat.format(currentTime)

    Printama.showPrinterList(webview.context!! as FragmentActivity,{
      Printama.with(context,{printama ->
        printama.connect({
          val LoreedoIsimpel = Printama.getBitmapFromVector(context, R.drawable.oreedo_isimpel)
          printama.printImage(LoreedoIsimpel,Printama.FULL_WIDTH,Printama.CENTER)
          printama.addNewLine()
          if(refID != null || refID != ""){
            printama.setSmallText()
            printama.printText("Ref No: $refID",Printama.CENTER)
            printama.addNewLine()
          }
          if(UserAuth.outletName != ""){
              printama.printTextBold(UserAuth.outletName,Printama.CENTER)
              printama.addNewLine()
            }
          if(locale!= null){
            var product_type = locale.get("product_type").asString
            printama.printTextBold(product_type,Printama.CENTER)
            printama.addNewLine()
          }
          printama.printText("Payment Receipt",Printama.CENTER)
          printama.addNewLine()
          for(i in translist){
            printama.addNewLine(2)
            var iObj = i.asJsonObject
            Log.d("test1234", "keyset ${iObj.keySet()}")
            var title = iObj.get("title").asString
            var valueCheck = iObj.get("value").isJsonArray
            printama.setSmallText()
            printama.printText(title)
            printama.addNewLine()
            printama.setNormalText()
            if(valueCheck){
              var value = iObj.getAsJsonArray("value")
              for(v in value){
                var vObj = v.asJsonObject
                if(value[0] != v){
                  printama.addNewLine()
                }
                printama.printTextBold("${vObj.get("title").asString}: ${vObj.get("value").asString}")
              }
            }else{
              var value = iObj.get("value").asString
              printama.printTextBold(value)
            }
          }
          if(hargaJual != ""){
            printama.addNewLine(2)
            printama.printTextNormal("TOTAL ",Printama.LEFT)
            printama.printTextBold( "Rp"+ hargaJual,Printama.RIGHT)
          }
          printama.addNewLine(2)
          printama.printText("Printed $formattedTime",Printama.CENTER)
          printama.feedPaper()
          printama.close()
        },{
        })
      })
    })



  }

  @JavascriptInterface
  fun showToast(text: String?){
    try {
      Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
    catch (e:Exception){

    }
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