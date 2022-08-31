package com.otto.sdk.shared.request

import kotlinx.serialization.Serializable

object Auth {
  @Serializable
  data class Request(
    val name: String,
    val secret_key: String,
    val device_id: String
  )
}
