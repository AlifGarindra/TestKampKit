package com.otto.sdk.shared.interfaces

import com.otto.sdk.shared.localData.UserInfoStatus

interface UserInfoListener {
  fun onUserInfo(userInfo:UserInfoStatus)
}