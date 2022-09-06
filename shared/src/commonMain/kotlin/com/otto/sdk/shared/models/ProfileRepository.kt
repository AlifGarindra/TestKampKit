package com.otto.sdk.shared.models

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import com.otto.sdk.shared.ApiConstant.INVALID_SESSION
import com.otto.sdk.shared.ApiConstant.OK
import com.otto.sdk.shared.DatabaseHelper
import com.otto.sdk.shared.ktor.ProfileApi
import com.otto.sdk.shared.response.Balance
import com.otto.sdk.shared.response.MasterMenu
import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import com.otto.sdk.shared.db.Profile
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileRepository(
  private val dbHelper: DatabaseHelper,
  private val settings: Settings,
  private val profile: ProfileApi,
  log: Logger,
  private val clock: Clock
) {
  private val log = log.withTag("ProfileMode")

  companion object {
    internal const val DB_TIMESTAMP_AUTH = "DbTimestampAuth"
    internal const val DB_TIMESTAMP_MASTER_MENU = "DbTimestampMasterMenu"
    internal const val DB_TIMESTAMP_BALANCE = "DbTimestampBalance"
  }

  init {
    ensureNeverFrozen()
  }

  fun selectAll(): Flow<List<Profile>> = dbHelper.selectAllProfile()
  fun selectAuth(): Flow<Profile> = dbHelper.selectProfileAuth()
  fun selectMasterMenu(): Flow<Profile> = dbHelper.selectProfileMasterMenu()
  fun selectProfile(): Flow<Profile> = dbHelper.selectProfileUser()

  suspend fun refreshAuthIfStale() {
    if (isAuthStale()) fetchAuth()
  }

  suspend fun refreshMasterMenuIfStale() {
    if (isMasterMenuStale()) fetchMasterMenu()
  }

  suspend fun fetchAuth() {
    val result: AuthToken.Result = profile.fetchAuth()
    settings.putLong(DB_TIMESTAMP_AUTH, clock.now().toEpochMilliseconds())
    when (result.code) {
      OK -> dbHelper.updateProfileAuth(Json.encodeToString(result))
      INVALID_SESSION -> log.e { "INVALID CREDENTIAL ${result.error?.message}" }
      else -> log.e { "UNKNOWN NET ERROR" }
    }
  }

  suspend fun submitLogin() {
    val result: AuthToken.Result =
      profile.submitLogin(
        "token here",
        "aditya.abdul@weekendinc.com",
        "Weekendinc123!"
      )
    settings.putLong(DB_TIMESTAMP_AUTH, clock.now().toEpochMilliseconds())
    when (result.code) {
      OK -> dbHelper.updateProfileAuth(Json.encodeToString(result))
      INVALID_SESSION -> log.e { "INVALID CREDENTIAL ${result.error?.message}" }
      else -> log.e { "UNKNOWN ERROR ${result.toString()}" }
    }
  }

  suspend fun fetchMasterMenu() {
    val result: MasterMenu.Result = profile.fetchMasterMenu()
    settings.putLong(DB_TIMESTAMP_MASTER_MENU, clock.now().toEpochMilliseconds())
    if (result.features.isNotEmpty())
      dbHelper.updateMasterMenu(Json.encodeToString(result))
  }

  suspend fun fetchBalance() {
    val result: Balance = profile.fetchBalance(
      "token here",
      "1"
    )
    settings.putLong(DB_TIMESTAMP_BALANCE, clock.now().toEpochMilliseconds())
    when (result.code) {
      OK -> dbHelper.updateBalance(Json.encodeToString(result))
      else -> log.e { "UMKNOWN ERROR ${result.toString()}" }
    }
  }

  private fun isAuthStale(): Boolean {
    val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_AUTH, 0)
    val oneHourMS = 100
    val stale = lastDownloadTimeMS + oneHourMS < clock.now().toEpochMilliseconds()
    if (!stale) {
      log.i { "auth not fetched from network. Recently update" }
    }
    return stale
  }

  private fun isMasterMenuStale(): Boolean {
    val lastDownloadTimeMS = settings.getLong(DB_TIMESTAMP_MASTER_MENU, 0)
    val oneMinuteMS = 0 * 60 * 1000
    val stale = lastDownloadTimeMS + oneMinuteMS < clock.now().toEpochMilliseconds()
    if (!stale) {
      log.i { "master menu not fetched from network. Recently update" }
    }
    return stale
  }
}