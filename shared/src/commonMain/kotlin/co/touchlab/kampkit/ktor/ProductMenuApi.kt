package co.touchlab.kampkit.ktor

import co.touchlab.kampkit.response.ProductMenuResult

interface ProductMenuApi {
  suspend fun getJsonFromApi(): ProductMenuResult
}