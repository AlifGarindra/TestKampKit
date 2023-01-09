package com.otto.sdk.shared.kampkit.android.data

object PpobUser {
  var clientToken : String = ""
  var userAccessToken : String = ""
  var refreshToken : String = ""

  fun reset(){
    clientToken = ""
    userAccessToken = ""
    refreshToken = ""
  }
}