package com.otto.sdk.shared.ktor

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import com.otto.sdk.shared.request.Auth
import com.otto.sdk.shared.response.Balance
import com.otto.sdk.shared.response.MasterMenu
import co.touchlab.stately.ensureNeverFrozen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import co.touchlab.kermit.Logger as KermitLogger

class ProfileApiImpl(
  private val log: KermitLogger,
  private val httpClient: HttpClient
) : ProfileApi {
  init {
    ensureNeverFrozen()
  }

  private fun HttpRequestBuilder.endpoint(path: String) = url {
    takeFrom("https://partner-service.wknd-otto.my.id")
    encodedPath = path
  }

  private fun HttpRequestBuilder.headerSession(token: String) {
    headers { append("wknd-token", token) }
  }

  override suspend fun fetchAuth(): AuthToken.Result = httpClient.post {
    endpoint("/api/token/get")
    contentType(ContentType.Application.Json)
    setBody(Auth.Request("admin", "admin123", "android"))
  }.body()

  override suspend fun submitLogin(
    tokenSession: String,
    email: String,
    pass: String
  ): AuthToken.Result =
    httpClient.submitForm(
      formParameters = Parameters.build {
        append("email", email)
        append("password", pass)
      }
    ) {
      endpoint("/api/token/login")
      headerSession(tokenSession)
    }.body()

  override suspend fun checkAuth(tokenSession: String): AuthToken.Result =
    httpClient.submitForm {
      endpoint("/api/token/check")
      headerSession(tokenSession)
    }.body()

  override suspend fun submitLogout(tokenSession: String): AuthToken.Result =
    httpClient.post {
      endpoint("/api/token/logout")
      headerSession(tokenSession)
    }.body()

  override suspend fun fetchMasterMenu(): MasterMenu.Result =
    httpClient.get("https://app.weekendinc.com/data/data.json").body()

  override suspend fun fetchBalance(tokenSession: String, userId: String): Balance =
    httpClient.submitForm(formParameters = Parameters.build {
      append("user_id", userId)
    }) {
      endpoint("/api/gold/check_balance_user")
      headerSession(tokenSession)
    }.body()
}