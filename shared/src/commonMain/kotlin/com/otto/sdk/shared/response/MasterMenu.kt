package com.otto.sdk.shared.response

import kotlinx.serialization.Serializable

object MasterMenu {

  @Serializable
  data class Result(
    val features: List<Feature>
  )

  @Serializable
  data class Feature(
    val item: List<Item>,
    val key: String
  )

  @Serializable
  data class Item(
    val code: String,
    val name: String,
    val rank: Int
  )
}