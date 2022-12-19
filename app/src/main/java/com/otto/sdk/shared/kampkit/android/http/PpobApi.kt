package com.otto.sdk.shared.kampkit.android.http

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.Instant

class PpobApi {
  val url : String = "https://gateway-dev.ottodigital.id/isimpel/v1"
  val okHttpClient = OkHttpClient()
  lateinit var request: Request
  val mediaType = "application/json; charset=utf-8".toMediaType()
  val rsaPpob = RsaPpob()

  fun getClientToken(){
    val jsonObject = JSONObject()
    jsonObject.put("grant_type", "client_credentials")
    jsonObject.put("scope", "PPOB-client")
    val body = jsonObject.toString().toRequestBody(mediaType)

    val nowdate : Long = Instant.now().epochSecond

    request = Request.Builder()
      .url(url+"/token")
      // .header("Authorization","Bearer 530d990e-12a0-3540-9eee-cac07233cf50")
      .header("X-TRACE-ID","40223460-1f34-4603-bf9f-7a6175cc602c")
      .header("X-TIMESTAMP","$nowdate")
      .header("X-SIGNATURE","${rsaPpob.getSignature(nowdate)}")
      .post(body)
      .build()

    okHttpClient.newCall(request).enqueue(object : Callback {
      override fun onFailure(call: Call, e: IOException) {
        Log.e("okhttpError", "onFailure:${e.message} ")
      }

      override fun onResponse(call: Call, response: Response) {
        if(!response.isSuccessful){
          Log.d("okhttp!success", "onResponse:${response} ")
        }else{
          val jsonData: String? = response?.body?.string()
          Log.d("okhttpSuccess", "onResponse:${jsonData} ")
          val Jobject = JSONObject(jsonData)
          if (Jobject.has("token")) {
            val token : JSONObject = Jobject.getJSONObject("token")
            if(token.has("access_token")){
              val clientToken : JSONObject = token.getJSONObject("access_token")
            }
          }
        }
      }
    })

  }
  fun getUserAccessToken(){

  }
  fun generateUserAccessToken(){

  }
}