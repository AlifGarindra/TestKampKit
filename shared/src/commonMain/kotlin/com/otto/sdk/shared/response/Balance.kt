package com.otto.sdk.shared.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Balance(
  @SerialName("user_id") val userId: String = "",
  @SerialName("corporate_id") val corporateId: String = "",
  @SerialName("balance") val balance: String = "",
  val code: Int = 0
)

