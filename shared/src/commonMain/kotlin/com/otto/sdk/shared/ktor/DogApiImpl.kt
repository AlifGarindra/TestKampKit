package com.otto.sdk.shared.ktor

import com.otto.sdk.shared.response.BreedResult
import co.touchlab.stately.ensureNeverFrozen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import co.touchlab.kermit.Logger as KermitLogger

class DogApiImpl(
  private val log: KermitLogger,
  private val httpClient: HttpClient
) : DogApi {

  init {
    ensureNeverFrozen()
  }

  override suspend fun getJsonFromApi(): BreedResult {
    log.d { "Fetching Breeds from network" }
    return httpClient.get {
      dogs("api/breeds/list/all")
    }.body()
  }

  private fun HttpRequestBuilder.dogs(path: String) {
    url {
      takeFrom("https://dog.ceo/")
      encodedPath = path
    }
  }
}
