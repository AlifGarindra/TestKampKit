package com.otto.sdk.shared.models

import co.touchlab.stately.ensureNeverFrozen
import com.otto.sdk.shared.ktor.PpobApi
import com.otto.sdk.shared.response.Posts
import com.otto.sdk.shared.response.UserInfoResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class PpobRepository(
  private val ppobApi: PpobApi
) {
  init {
    ensureNeverFrozen()
  }

  // fun fetchUserInfo(timeStamp:String,clientToken:String,phoneNumber:String) : UserInfoResult{
  //   val asyncUserInfo : UserInfoResult =  runBlocking {
  //     var userInfo = async {
  //     ppobApi.getUserInfo(timeStamp,clientToken,phoneNumber)
  //     }
  //     userInfo.await()
  //   }
  //   return asyncUserInfo
  // }

  fun fetchUserInfo(timeStamp:String,userToken:String,phoneNumber:String,onResponse:(Int,UserInfoResult)->Unit){
    val userInfoResponse : HttpResponse =  runBlocking {
      var userInfo = async {
        ppobApi.getUserInfo(timeStamp,userToken,phoneNumber)
      }
      userInfo.await()
    }
    var userInfoBody : UserInfoResult = runBlocking {
      userInfoResponse.body()
    }
    onResponse(userInfoResponse.status.value,userInfoBody)
  }
}