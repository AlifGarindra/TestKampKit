package com.otto.sdk.shared.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResult(
  val meta : Meta,
  val data : PpobData.UserInfo,
)
