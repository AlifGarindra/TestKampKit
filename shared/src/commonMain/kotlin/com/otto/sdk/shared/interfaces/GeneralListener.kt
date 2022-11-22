package com.otto.sdk.shared.interfaces

import com.otto.sdk.shared.response.GeneralStatus

interface GeneralListener {
fun onOpenPPOB( status : GeneralStatus)
fun onClosePPOB(status : GeneralStatus)
fun onError(status : GeneralStatus)
fun onAccessTokenRemoved(status : GeneralStatus){
  // misalnya gaperlu di implement
}
}