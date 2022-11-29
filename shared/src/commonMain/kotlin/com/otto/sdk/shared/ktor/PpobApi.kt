package com.otto.sdk.shared.ktor

import com.otto.sdk.shared.response.UserInfoResult

interface PpobApi {
  suspend fun getUserInfo(timeStamp:String,clientKey:String) : UserInfoResult
}