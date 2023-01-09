package com.otto.sdk.shared.localData

object UserInfoStatus {
  var accountId :String = ""
  var phoneNumber: String = ""
  var balance : String = ""

  fun reset(){
    accountId = ""
    phoneNumber=""
    balance=""
  }
}