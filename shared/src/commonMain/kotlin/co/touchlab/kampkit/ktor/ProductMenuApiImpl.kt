package co.touchlab.kampkit.ktor

import co.touchlab.kampkit.response.ProductMenuResult
import co.touchlab.stately.ensureNeverFrozen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

class ProductMenuApiImpl(
  private val log: KermitLogger,
  private val httpClient: HttpClient
) : ProductMenuApi {

  init {
    ensureNeverFrozen()
  }

  override suspend fun getJsonFromApi(): ProductMenuResult {
    log.d { "Fetching Product Menu from network" }
    log.d { "Fetching Product Menu from network" }
    log.d { "Fetching Product Menu from network" }
    log.d { "Fetching Product Menu from network" }
    log.d { "Fetching Product Menu from network" }
    log.d { "Fetching Product Menu from network" }
    return httpClient.get {
      productMenu("data/productmenu.json")
    }.body()
  }

  private fun HttpRequestBuilder.productMenu(path: String) {
    url {
      takeFrom("https://app.weekendinc.com/")
      encodedPath = path
    }
  }
}