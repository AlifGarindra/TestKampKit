package com.otto.sdk.shared.interfaces

import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.GeneralStatus

interface GeneralListener {
fun onOpenPPOB( status : GeneralStatus)
fun onClosePPOB(status : GeneralStatus)
fun onError(status : ErrorStatus)
fun onClientTokenExpired()
fun onUserAccessTokenExpired()
fun onAuthCode(authCode:String)
fun onUserProfile(){
    // misalnya gaperlu di implement
}
}