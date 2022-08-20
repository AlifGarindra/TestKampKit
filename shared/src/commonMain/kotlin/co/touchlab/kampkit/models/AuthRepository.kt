package co.touchlab.kampkit.models

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import co.touchlab.kampkit.ApiConstant.INVALID_AUTH
import co.touchlab.kampkit.ApiConstant.OK
import co.touchlab.kampkit.DatabaseHelper
import co.touchlab.kampkit.db.Auth
import co.touchlab.kampkit.ktor.AuthApi
import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class AuthRepository(
  private val dbHelper: DatabaseHelper,
  private val settings: Settings,
  private val authApi: AuthApi,
  log: Logger,
  private val clock: Clock
) {
  private val log = log.withTag("AuthMenuModel")

  companion object {
    internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
  }

  init {
    ensureNeverFrozen()
  }

  fun getAuths(): Flow<List<Auth>> = dbHelper.selectAll()

  suspend fun refreshAuthIfStale() {
    if (isAuthStale()) {
      getAuth()
    }
  }

  suspend fun getAuth() {
    val result: AuthToken.Result = authApi.getToken()
    log.v { "Auth net. result [token_code]: ${result.data.tokenCode}" }
    settings.putLong(DB_TIMESTAMP_KEY, clock.now().toEpochMilliseconds())
    when (result.code.toInt()) {
      OK -> dbHelper.insertAuth(result)
      INVALID_AUTH -> log.e { "INVALID_AUTH: ${result.error?.message}" }
      else -> log.e { "AUTH ERROR" }
    }
  }

  suspend fun refreshAuth() {
    getAuth()
    TODO("refresh auth api not yet available, for now just call getAuth")
  }

  private fun isAuthStale(): Boolean {
    val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
    val oneHourMS = 100
    val stale = lastDownloadTimeMS + oneHourMS < clock.now().toEpochMilliseconds()
    if (!stale) {
      log.i { "Auth not fetched from network. Recently update" }
    }
    return stale
  }
}