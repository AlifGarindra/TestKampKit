package co.touchlab.kampkit.response

import kotlinx.serialization.Serializable

@Serializable
data class ProductMenuResult(
  val message: List<ProductMenuItem>,
  var status: String
)

@Serializable
data class ProductMenuItem(
  val rank: Int,
  val code: String,
  val name: String
)