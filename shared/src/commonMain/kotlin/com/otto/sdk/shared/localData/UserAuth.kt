package com.otto.sdk.shared.localData

object UserAuth {

  var clientToken : String = ""
  var phoneNumber : String = ""
  var outletName:  String = ""
  var userAccessToken : String = ""

  fun reset(){
    clientToken = ""
    phoneNumber = ""
    outletName = ""
    userAccessToken = ""
  }
}