package co.AuthTokenResulttouchlab.kampkit.response

import kotlinx.serialization.Serializable

object AuthToken {

  @Serializable
  data class Result(
    val apiVersion: String,
    val memoryUsage: String,
    val elapseTime: String,
    val lang: String,
    val code: Short,
    val error: Error?,
    val data: Auth
  )

  @Serializable
  data class Auth(
    val id: Long,
    val appsId: String,
    val deviceId: String,
    val deviceType: String,
    val tokenCode: String,
    val refreshToken: String,
    val createdDate: String,
    val expiredDate: String,
    val tokenProfile: TokenProfile
  )

  @Serializable
  data class TokenProfile(
    val userId: String,
    val adminId: String,
    val lastActivity: String
  )

  @Serializable
  data class Error(
    val message: String,
    val errors: List<ErrorItem>
  )

  @Serializable
  data class ErrorItem(
    val code: Short,
    val message: String
  )
}