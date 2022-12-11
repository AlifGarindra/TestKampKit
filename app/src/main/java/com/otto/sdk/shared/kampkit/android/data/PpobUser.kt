package com.otto.sdk.shared.kampkit.android.data

object PpobUser {
  var clientToken : String = ""
  var userAccessToken : String = ""

  fun reset(){
    clientToken = ""
    userAccessToken = ""
  }
}