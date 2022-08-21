package co.touchlab.kampkit.ktor

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import co.touchlab.kampkit.request.Auth
import co.touchlab.kampkit.response.AuthLoginResult
import co.touchlab.stately.ensureNeverFrozen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import co.touchlab.kermit.Logger as KermitLogger

class AuthApiImpl(
  private val log: KermitLogger,
  private val httpClient: HttpClient
) : AuthApi {

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
    setBody(
      Auth.Request(
        "admin",
        "admin123",
        "android"
      )
    )
  }.body()

  override suspend fun doLogin(): AuthLoginResult = httpClient.submitForm(
    formParameters = Parameters.build {
      append("email", "aditya.abdul@weekendinc.com")
      append("password", "Weekendinc123!")
    }) {
    endpoint("/api/token/login")
    headerSession("sGpQjtKHhn0wqHArcLS1vRRTbZZnlyro")
  }.body()
}