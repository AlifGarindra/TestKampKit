package com.otto.sdk.shared.ktor

import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import com.otto.sdk.shared.response.Posts
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom

class PostsApiImpl(
  private val log: Logger,
  private val httpClient: HttpClient
) : PostApi {

  init {
    ensureNeverFrozen()
  }

  private fun HttpRequestBuilder.endpoint(path: String) = url {
    takeFrom("https://jsonplaceholder.typicode.com")
    encodedPath = path
  }

  override suspend fun getSinglePost(): Posts {
    return httpClient.get {
      endpoint("/posts/1")
    }.body()
  }
}