package com.otto.sdk.shared.request

import kotlinx.serialization.Serializable

object TokenRequest{
  @Serializable
  data class getClient(
    val grantType : String,
    val scope : String?,
  )

  @Serializable
  data class generateUserAccess(
    val grantType : String,
    val authCode : String,
    val phoneNumber : String,
  )

  @Serializable
  data class refreshUserAccess(
    val grantType: String,
    val refreshToken : String,
  )
}

