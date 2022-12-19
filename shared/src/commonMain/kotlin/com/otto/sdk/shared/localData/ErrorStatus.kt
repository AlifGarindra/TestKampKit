package com.otto.sdk.shared.localData

object ErrorStatus {
  var type : String= ""
  var code :String = ""
  var message : String = ""

  fun reset(){
    type = ""
    code = ""
    message = ""
  }
}