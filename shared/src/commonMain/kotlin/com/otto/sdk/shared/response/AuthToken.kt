package co.AuthTokenResulttouchlab.kampkit.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object AuthToken {

  @Serializable
  data class Result(
    @SerialName("api_version") val apiVersion: String = "",
    @SerialName("memory_usage") val memoryUsage: String = "",
    @SerialName("elapse_time") val elapseTime: String = "",
    val lang: String = "",
    val code: Int = 1,
    val error: Error? = null,
    val data: Auth
  )

  @Serializable
  data class Auth(
    val id: Long = 0L,
    @SerialName("apps_id") val appsId: String = "",
    @SerialName("device_id") val deviceId: String = "",
    @SerialName("device_type") val deviceType: String = "",
    @SerialName("token_code") val tokenCode: String = "",
    @SerialName("refresh_token") val refreshToken: String = "",
    @SerialName("user_id") val userId: String = "",
    @SerialName("created_date") val createdDate: String = "",
    @SerialName("expired_date") val expiredDate: String = "",
    @SerialName("token_profile") val tokenProfile: TokenProfile? = null
  )

  @Serializable
  data class TokenProfile(
    @SerialName("user_id") val userId: String = "",
    @SerialName("admin_id") val adminId: String = "",
    @SerialName("last_activity") val lastActivity: String = ""
  )

  @Serializable
  data class Error(
    val message: String = "",
    val errors: List<ErrorItem> = arrayListOf()
  )

  @Serializable
  data class ErrorItem(
    val code: Short,
    val message: String
  )
}