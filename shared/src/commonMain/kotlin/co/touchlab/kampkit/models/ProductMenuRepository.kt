package co.touchlab.kampkit.models

import co.touchlab.kampkit.DatabaseHelper
import co.touchlab.kampkit.db.ProductMenu
import co.touchlab.kampkit.ktor.ProductMenuApi
import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class ProductMenuRepository(
  private val dbHelper: DatabaseHelper,
  private val settings: Settings,
  private val productMenuApi: ProductMenuApi,
  log: Logger,
  private val clock: Clock
) {
  private val log = log.withTag("ProductMenuModel")

  companion object {
    internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
  }

  init {
    ensureNeverFrozen()
  }

  fun getProductMenu(): Flow<List<ProductMenu>> = dbHelper.selectAllProductMenu();

  suspend fun refreshProductMenuIfStale() {
    if (isProductMenuStale()) {
      refreshProductMenu()
    }
  }

  suspend fun refreshProductMenu() {
    val productMenuResult = productMenuApi.getJsonFromApi();
    log.v { "ProductMenu network result: ${productMenuResult.status}" }
    val productMenuList = productMenuResult.message;
    log.v { "Fetched ${productMenuList.size} list of productMenu from network" }
    settings.putLong(DB_TIMESTAMP_KEY, clock.now().toEpochMilliseconds())

    if (productMenuList.isNotEmpty()) {
      dbHelper.insertProductMenuList(productMenuList = productMenuList)
    }
  }

  private fun isProductMenuStale(): Boolean {
    val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
    // val oneHourMS = 60 * 60 * 1000
    val oneHourMS = 100
    val stale = lastDownloadTimeMS + oneHourMS < clock.now().toEpochMilliseconds()
    if (!stale) {
      log.i { "ProductMenu List not fetched from network. Recently updated" }
    }
    return stale
  }
}