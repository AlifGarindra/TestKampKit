package co.touchlab.kampkit.ktor

import co.touchlab.kampkit.response.ProductMenuResult
import co.touchlab.stately.ensureNeverFrozen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import co.touchlab.kermit.Logger as KermitLogger

class ProductMenuApiImpl(
  private val log: KermitLogger,
  private val httpClient: HttpClient
) : ProductMenuApi {

  init {
    ensureNeverFrozen()
  }

  override suspend fun getJsonFromApi(): ProductMenuResult {
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