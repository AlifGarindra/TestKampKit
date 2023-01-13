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
import java.util.UUID

class PpobApi {
  val url : String = "https://gateway-dev.ottodigital.id/isimpel/v1"
  val okHttpClient = OkHttpClient()
  lateinit var request: Request
  val mediaType = "application/json; charset=utf-8".toMediaType()
  val rsaPpob = RsaPpob()

  fun getClientToken(accessToken:(String)->Unit){
    val jsonObject = JSONObject()
    jsonObject.put("grant_type", "client_credentials")
    jsonObject.put("scope", "PPOB-client")
    val body = jsonObject.toString().toRequestBody(mediaType)
    var uuid = UUID.randomUUID().toString()
    val nowdate : Long = System.currentTimeMillis() / 1000

    request = Request.Builder()
      .url(url+"/token")
      // .header("Authorization","Bearer 530d990e-12a0-3540-9eee-cac07233cf50")
      // .header("Authorization","Basic ej0q9anMD2xThWZH2s9EEcVbBg8a:H67QPXbzIb7eEVhLg0PHHaTOwr8a")
      .header("Authorization","Basic ZWowcTlhbk1EMnhUaFdaSDJzOUVFY1ZiQmc4YTpINjdRUFhiekliN2VFVmhMZzBQSEhhVE93cjhh")
      .header("X-TRACE-ID",uuid)
      .header("X-TIMESTAMP","$nowdate")
      .header("X-SIGNATURE","${rsaPpob.getSignature(nowdate, "{\"grant_type\":\"client_credentials\",\"scope\":\"PPOB-client\"}")}")
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
              val clientToken : String = token.getString("access_token")
              accessToken(clientToken)
            }
          }
        }
      }
    })

  }
  fun getUserAccessToken(){

  }

  fun generateUserAccessToken(clientToken:String,phoneNumber: String,authCode:String,accessToken: (userToken:String,refreshToken:String) -> Unit){
    val jsonObject = JSONObject()
    jsonObject.put("auth_code", authCode)
    val body = jsonObject.toString().toRequestBody(mediaType)
    var uuid = UUID.randomUUID().toString()
    val nowdate : Long = System.currentTimeMillis() / 1000

    request = Request.Builder()
      .url(url+"/accounts/${phoneNumber}/auth-code/validation")
      .header("Authorization","Bearer $clientToken")
      .header("X-TRACE-ID",uuid)
      .header("X-TIMESTAMP","$nowdate")
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
          var userToken : String = ""
          var refreshToken : String = ""
          val jsonData: String? = response?.body?.string()
          Log.d("okhttpSuccess", "onResponse:${jsonData} ")
          val Jobject = JSONObject(jsonData)
          if (Jobject.has("token")) {
            val token : JSONObject = Jobject.getJSONObject("token")
            if(token.has("access_token")){
               userToken = token.getString("access_token")
            }
            if(token.has("refresh_token")){
             refreshToken = token.getString("refresh_token")
            }
            accessToken(userToken,refreshToken)
          }
        }
      }
    })
  }


  fun refreshUserAccessToken(clientToken:String,refreshToken: String,accessToken: (userToken:String,refreshToken:String) -> Unit){
    val jsonObject = JSONObject()
    jsonObject.put("grant_type","refresh_token")
    jsonObject.put("refresh_token", refreshToken)
    val body = jsonObject.toString().toRequestBody(mediaType)
    var uuid = UUID.randomUUID().toString()
    val nowdate : Long = System.currentTimeMillis() / 1000

    request = Request.Builder()
      .url(url+"/token")
      .header("Authorization","Bearer $clientToken")
      .header("X-TRACE-ID",uuid)
      .header("X-TIMESTAMP","$nowdate")
      .header("X-SIGNATURE","${rsaPpob.getSignature(nowdate, "{\"grant_type\":\"refresh_token\",\"refresh_token\":\"${refreshToken}\"}")}")
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
          var userToken : String = ""
          var refreshToken : String = ""
          val jsonData: String? = response?.body?.string()
          Log.d("okhttpSuccess", "onResponse:${jsonData} ")
          val Jobject = JSONObject(jsonData)
          if (Jobject.has("token")) {
            val token : JSONObject = Jobject.getJSONObject("token")
            if(token.has("access_token")){
              userToken = token.getString("access_token")
            }
            if(token.has("refresh_token")){
              refreshToken = token.getString("refresh_token")
            }
            accessToken(userToken,refreshToken)
          }
        }
      }
    })
  }
}