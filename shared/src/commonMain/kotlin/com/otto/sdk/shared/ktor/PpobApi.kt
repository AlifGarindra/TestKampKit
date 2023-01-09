package com.otto.sdk.shared.ktor

import com.otto.sdk.shared.response.UserInfoResult
import io.ktor.client.statement.HttpResponse

interface PpobApi {
  suspend fun getUserInfo(timeStamp:String,userToken:String,phoneNumber:String) : HttpResponse
}