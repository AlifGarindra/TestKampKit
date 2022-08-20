package co.touchlab.kampkit.ktor

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import co.touchlab.kampkit.response.AuthLoginResult
import co.touchlab.stately.ensureNeverFrozen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.serialization.Serializable
import co.touchlab.kermit.Logger as KermitLogger

class AuthApiImpl(
  private val log: KermitLogger,
  private val httpClient: HttpClient
) : AuthApi {

  init {
    ensureNeverFrozen()
  }

  private fun HttpRequestBuilder.authToken(path: String) {
    url {
      takeFrom("https://partner-service.wknd-otto.my.id")
      // takeFrom("http://192.168.1.10:3005")
      encodedPath = path
    }
    // headersOf("Content-Type", "application/json")
  }

  override suspend fun getToken(): AuthToken.Result {
    log.d { "Fetching Token" }
    log.d { "Fetching Token" }

    // val response: HttpResponse = httpClient.submitForm(
    //   formParameters = Parameters.build {
    //     append("name", "admin")
    //     append("device_id", "android")
    //     append("secret_key", "admin123")
    //   }) {
    //   authToken("/api/token/get")
    // }
    // return response.body()
    @Serializable
    data class Credential(val name: String, val secret_key: String, val device_id: String)

    return httpClient.post {
      authToken("/api/token/get")
      contentType(ContentType.Application.Json)
      setBody(
        Credential(
          "admin",
          "admin123",
          "kotlin",
        )
      )
    }.body()
  }

  override suspend fun doLogin(): AuthLoginResult {
    log.d { "SubmitLogin" }
    return httpClient.post {
      authToken("login")
    }.body()
  }
}