package co.touchlab.kampkit.models

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import co.touchlab.kampkit.ApiConstant.INVALID_SESSION
import co.touchlab.kampkit.ApiConstant.OK
import co.touchlab.kampkit.DatabaseHelper
import co.touchlab.kampkit.db.UserProfile
import co.touchlab.kampkit.ktor.UserProfileApi
import co.touchlab.kampkit.response.Balance
import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserProfileRepository(
  private val dbHelper: DatabaseHelper,
  private val settings: Settings,
  private val userProfileApi: UserProfileApi,
  log: Logger,
  private val clock: Clock
) {
  private val log = log.withTag("UserProfileMode")

  companion object {
    internal const val DB_TIMESTAMP_KEY = "DbTimestampKey"
  }

  init {
    ensureNeverFrozen()
  }

  fun selectAll(): Flow<List<UserProfile>> = dbHelper.selectAllProfile()
  fun selectAuth(): Flow<UserProfile> = dbHelper.selectProfileAuth()
  fun selectProfile(): Flow<UserProfile> = dbHelper.selectProfileUser()
  suspend fun refreshAuthIfStale() {
    if (isAuthStale()) fetchAuth()
  }

  suspend fun fetchAuth() {
    val result: AuthToken.Result = userProfileApi.fetchAuth()
    settings.putLong(DB_TIMESTAMP_KEY, clock.now().toEpochMilliseconds())
    when (result.code) {
      OK -> dbHelper.updateProfileAuth(Json.encodeToString(result))
      INVALID_SESSION -> log.e { "INVALID CREDENTIAL ${result.error?.message}" }
      else -> log.e { "UNKNOWN NET ERROR" }
    }
  }

  suspend fun submitLogin() {
    val result: AuthToken.Result =
      userProfileApi.submitLogin(
        "token here",
        "aditya.abdul@weekendinc.com",
        "Weekendinc123!"
      )
    settings.putLong(DB_TIMESTAMP_KEY, clock.now().toEpochMilliseconds())
    when (result.code) {
      OK -> dbHelper.updateProfileAuth(Json.encodeToString(result))
      INVALID_SESSION -> log.e { "INVALID CREDENTIAL ${result.error?.message}" }
      else -> log.e { "UNKNOWN ERROR ${result.toString()}" }
    }
  }

  suspend fun fetchBalance() {
    val result: Balance = userProfileApi.fetchBalance(
      "token here",
      "1"
    )
    settings.putLong(DB_TIMESTAMP_KEY, clock.now().toEpochMilliseconds())
    when (result.code) {
      OK -> dbHelper.updateBalance(Json.encodeToString(result))
      else -> log.e { "UMKNOWN ERROR ${result.toString()}" }
    }
  }

  private fun isAuthStale(): Boolean {
    val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_KEY, 0)
    val oneHourMS = 100
    val stale = lastDownloadTimeMS + oneHourMS < clock.now().toEpochMilliseconds()
    if (!stale) {
      log.i { "auth not fetched from network. Recently update" }
    }
    return stale
  }
}