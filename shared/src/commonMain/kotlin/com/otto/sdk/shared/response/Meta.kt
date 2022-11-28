package com.otto.sdk.shared.response

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
  val code : String?,
  val title : String?,
  val message : String?,
)
