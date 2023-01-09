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
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PpobApiImpl(
  private val log: Logger,
  private val httpClient: HttpClient
):PpobApi {

  init {
    ensureNeverFrozen()
  }

  private fun HttpRequestBuilder.endpoint(path: String) = url {
    takeFrom("https://gateway-dev.ottodigital.id")
    encodedPath = path
  }

  //9bfb19b9-10e3-3168-9325-ed7073482160

  override suspend fun getUserInfo(timeStamp:String,userToken:String,phoneNumber:String): HttpResponse {
    return httpClient.get {
    headers{
      append(HttpHeaders.Authorization,"Bearer $userToken")
      append(HttpHeaders.ContentType,"application/json")
      append("X-TIMESTAMP",timeStamp)
      append("X-TRACE-ID","531e427d-2c99-4305-8cbb-5fcfa323d2f4")
    }
      endpoint("/isimpel/v1/accounts/$phoneNumber")
    }
  }


// override suspend fun getUserInfo(timeStamp:String,clientToken:String,phoneNumber:String): UserInfoResult {
//  var userInfoResponse =  httpClient.get {
//     headers{
//       append(HttpHeaders.Authorization,"Bearer $clientToken")
//       append(HttpHeaders.ContentType,"application/json")
//       append("X-TIMESTAMP",timeStamp)
//       append("X-TRACE-ID","531e427d-2c99-4305-8cbb-5fcfa323d2f4")
//     }
//     endpoint("/isimpel/v1/accounts/$phoneNumber")
//   }.bodyAsText()
//   var responseBody : UserInfoResult = Json {
//     ignoreUnknownKeys = true
//     isLenient = true
//   }.decodeFromString(userInfoResponse)
//   return responseBody
// }
}