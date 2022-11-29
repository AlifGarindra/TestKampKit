package com.otto.sdk.shared.ktor

import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import com.otto.sdk.shared.response.UserInfoResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class PpobApiImpl(
  private val log: Logger,
  private val httpClient: HttpClient
):PpobApi {

  init {
    ensureNeverFrozen()
  }

  private fun HttpRequestBuilder.endpoint(path: String) = url {
    takeFrom("https://portal.ottodigital.id/simulator/webhook")
    encodedPath = path
  }

  override suspend fun getUserInfo(timeStamp:String,clientKey:String): UserInfoResult {
    return httpClient.get {
    headers{
      append(HttpHeaders.Authorization,"token")
      append(HttpHeaders.ContentType,"application/json")
      append("X-TIMESTAMP",timeStamp)
      append("X-Client-Key",clientKey)
    }
      endpoint("/is-get-user-balance")
    }.body()
  }
}