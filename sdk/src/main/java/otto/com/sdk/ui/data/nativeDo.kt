package otto.com.sdk.ui.data

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.anggastudio.printama.Printama
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.otto.sdk.shared.localData.ErrorStatus
import org.json.JSONObject
import otto.com.sdk.SDKManager
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

class nativeDo(var context : Context,var webview:WebView) : AppCompatActivity() {
  var value : String = ""
  var generalListener = SDKManager.getInstance(context).generalListener

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

  @RequiresApi(Build.VERSION_CODES.M)
  @JavascriptInterface
  fun onPrintHistory(data:String?,hargaJual:String? = "0"){

    checkBluetooth()
    var jElm: JsonElement = JsonParser.parseString(data)
    var jObj : JsonObject = jElm.asJsonObject
    var transArray = jObj.getAsJsonArray("transaction_array")
    var translist = transArray.asList()

    Printama.showPrinterList(webview.context!! as FragmentActivity,{
      Printama.with(context,{printama ->
        printama.connect({
          for(i in translist){
            var iObj = i.asJsonObject
            var title = iObj.get("title").asString
            var value = iObj.get("value").asString
            printama.printTextJustify("$title : ",value)
          }
          printama.setNormalText()
          printama.feedPaper()
          printama.close()
        },{
        })
      })
    })


    // OLD CODES
    // var customer_number : String = ""
    // var customer_name : String = ""
    // var customer_server : String = ""
    // var period : String = ""
    // var member_total = ""
    // var product : String = ""
    // var transaction_time : String = ""
    //
    // var jElm: JsonElement = JsonParser.parseString(data)
    // var jObj : JsonObject = jElm.asJsonObject

    // for (i in jObj.entrySet()){
    //   Log.d("test1234", "onPrintHistory:${i.key} ${i.value}")
    //   when(i.key.toString()){
    //     "product"->{
    //       if(i.value.isJsonObject){
    //          var productObj = i.value.asJsonObject
    //         product = productObj.get("denom_name").asString
    //       }
    //     }
    //     "transaction_attributes" ->{
    //       if(i.value.isJsonObject){
    //         var transAtt = i.value.asJsonObject
    //         customer_number = transAtt.get("customer_number").asString
    //         customer_name = transAtt.get("customer_name").asString
    //         if(transAtt.has("server_id")){
    //           customer_server = transAtt.get("server_id").asString
    //         }
    //         if(transAtt.has("period")){
    //           period = transAtt.get("period").asString
    //         }
    //         if(transAtt.has("member_total")){
    //           member_total = transAtt.get("member_total").asString
    //         }
    //
    //       }
    //     }
    //     "transaction_confirmed_at" ->{
    //       val dateString = i.value.asString
    //       val formatter = DateTimeFormatterBuilder()
    //         .appendPattern("d MMM yyyy - HH:mm")
    //         .toFormatter(Locale.ENGLISH)
    //       val dateTime = OffsetDateTime.parse(dateString).atZoneSameInstant(ZoneId.systemDefault()).format(formatter)
    //       transaction_time = dateTime
    //     }
    //   }
    // }



    // Printama.showPrinterList(webview.context!! as FragmentActivity,{
    //   Printama.with(context,{printama ->
    //     printama.connect({
    //       if(customer_number!=""){
    //         var chunkedNumber = customer_number.chunked(20)
    //         printama.printTextJustify("Nomor : ", chunkedNumber[0])
    //         for(i in chunkedNumber){
    //           if(chunkedNumber.indexOf(i) != 0){
    //             printama.addNewLine()
    //             printama.printText(i,Printama.RIGHT)
    //           }
    //       }
    //         printama.addNewLine()
    //       }
    //
    //       if(customer_name!=""){
    //         var chunkedName = customer_name.chunked(20)
    //         printama.printTextJustify("Nama : ",chunkedName[0])
    //         for(i in chunkedName){
    //           if(chunkedName.indexOf(i) != 0){
    //             printama.addNewLine()
    //             printama.printText(i,Printama.RIGHT)
    //           }
    //         }
    //         printama.addNewLine()
    //       }
    //
    //       if(customer_server!=""){
    //         printama.printTextJustify("Server ID : ", customer_server)
    //       }
    //       if(period!=""){
    //         printama.printTextJustify("Jml Bulan : ", period)
    //       }
    //       if(member_total!=""){
    //         printama.printTextJustify("Jml Peserta : ", member_total)
    //       }
    //       if(product != ""){
    //         printama.printTextJustify("Produk : ", product)
    //       }
    //       if(transaction_time!= ""){
    //         printama.printTextJustify("Waktu : ", transaction_time)
    //       }
    //       if(hargaJual != ""){
    //         printama.addNewLine(2)
    //         printama.printTextJustify("Total Harga : ", "Rp "+ hargaJual)
    //       }
    //       printama.setNormalText()
    //       printama.feedPaper()
    //       printama.close()
    //     },{
    //     })
    //   })
    // })





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