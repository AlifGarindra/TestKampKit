package com.otto.sdk.shared.interfaces

import com.otto.sdk.shared.localData.GeneralStatus

interface GeneralListener {
fun onOpenPPOB( status : GeneralStatus)
fun onClosePPOB(status : GeneralStatus)
fun onError(status : GeneralStatus)
fun onUserAccessTokenEmpty(status : GeneralStatus){
  // misalnya gaperlu di implement
}
  fun onClientTokenEmpty(status : GeneralStatus){
    // misalnya gaperlu di implement
  }

  fun phoneNumberEmpty(status : GeneralStatus){
    // misalnya gaperlu di implement
  }

  fun onClientTokenExpired(){
    // misalnya gaperlu di implement
  }
  fun onUserAccessTokenExpired()
  fun onAuthCode(){
    // misalnya gaperlu di implement
  }
  fun onUserProfile(){
    // misalnya gaperlu di implement
  }
}