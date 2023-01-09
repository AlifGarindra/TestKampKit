package com.otto.sdk.shared.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object PpobData {

  @Serializable
  data class UserInfo(
    val account_id : String?,
    val mobile_phone_number : String?,
    val balance_amount : Int?,
  )

  @Serializable
  data class Token(
    @SerialName("accessToken") val access_token : String?,
    @SerialName("refreshToken") val refresh_token : String?,
    val scope : String?,
    @SerialName("tokenType") val token_type: String?,
    @SerialName("expiresIn") val expires_in : String?,
  )

}