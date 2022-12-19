package com.otto.sdk.shared.interfaces

import com.otto.sdk.shared.localData.ErrorStatus
import com.otto.sdk.shared.localData.GeneralStatus
import com.otto.sdk.shared.localData.UserInfoStatus

interface GeneralListener {
fun onOpenPPOB( status : GeneralStatus)
fun onClosePPOB(status : GeneralStatus)
fun onError(status : ErrorStatus)
fun onClientTokenExpired()
fun onUserAccessTokenExpired()
fun onAuthCode(authCode:String)
fun onUserProfile(userInfo : UserInfoStatus)
}