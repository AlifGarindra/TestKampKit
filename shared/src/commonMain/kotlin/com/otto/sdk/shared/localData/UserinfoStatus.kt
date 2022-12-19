package com.otto.sdk.shared.localData

object UserInfoStatus {
  var phoneNumber: String = ""
  var balance : String = ""

  fun reset(){
    phoneNumber=""
    balance=""
  }
}