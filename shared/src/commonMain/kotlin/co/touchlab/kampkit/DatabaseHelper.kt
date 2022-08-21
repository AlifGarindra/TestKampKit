package co.touchlab.kampkit

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import co.touchlab.kampkit.db.Auth
import co.touchlab.kampkit.db.Breed
import co.touchlab.kampkit.db.KaMPKitDb
import co.touchlab.kampkit.db.ProductMenu
import co.touchlab.kampkit.db.UserProfile
import co.touchlab.kampkit.response.ProductMenuItem
import co.touchlab.kampkit.sqldelight.transactionWithContext
import co.touchlab.kermit.Logger
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DatabaseHelper(
  sqlDriver: SqlDriver,
  private val log: Logger,
  private val backgroundDispatcher: CoroutineDispatcher
) {
  private val dbRef: KaMPKitDb = KaMPKitDb(sqlDriver)

  fun selectAllItems(): Flow<List<Breed>> =
    dbRef.tableQueries
      .selectAll()
      .asFlow()
      .mapToList()
      .flowOn(backgroundDispatcher)

  fun selectAllProductMenu(): Flow<List<ProductMenu>> =
    dbRef.productMenuQueries.selectAll().asFlow().mapToList()
      .flowOn(backgroundDispatcher)

  suspend fun insertBreeds(breeds: List<String>) {
    log.d { "Inserting ${breeds.size} breeds into database" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      breeds.forEach { breed ->
        dbRef.tableQueries.insertBreed(breed)
      }
    }
  }

  suspend fun insertProductMenuList(productMenuList: List<ProductMenuItem>) {
    log.d("Inserting ${productMenuList.size} productMenuList into database")
    dbRef.transactionWithContext(backgroundDispatcher) {
      productMenuList.forEach { productMenu ->
        dbRef.productMenuQueries.insertProductMenu(
          rank = productMenu.rank,
          code = productMenu.code,
          name = productMenu.name
        )
      }
    }
  }

  suspend fun insertAuth(result: AuthToken.Result) {
    log.d { "Inserting ${result.data.tokenCode}, user_id ${result.data.tokenProfile?.userId}" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      when (result.error) {
        null -> ""
        else -> result.error.message
      }.let {
        result.data.tokenProfile?.let { it1 ->
          dbRef.authQueries.insertAuth(
            user_id = result.data.tokenProfile?.userId,
            apps_id = result.data.appsId,
            device_id = result.data.deviceId,
            device_type = result.data.deviceType,
            token_code = result.data.tokenCode,
            refresh_token = result.data.refreshToken,
            created_date = result.data.createdDate,
            expired_date = result.data.expiredDate,
            last_activity = it1.lastActivity,
            error_code = result.code.toLong(),
            error_message = it
            // error_code = result.code,
          )
        }
      }
    }
  }

  fun selectProfileAuth(): Flow<UserProfile> =
    dbRef.userProfileQueries
      .selectAuth()
      .asFlow()
      .mapToOne()
      .flowOn(backgroundDispatcher)

  suspend fun updateProfileAuth(json: String) {
    log.i { "UserProfile $json" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.userProfileQueries.updateAuth("{{{}}}")
    }
  }

  suspend fun updateProfile(json: String) {
    log.i { "UserProfile update $json" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.userProfileQueries.updateProfile(json)
    }
  }

  suspend fun updateBalance(json: String) {
    log.i { "Balance update ${json}" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.userProfileQueries.updateSaldo(json)
    }
  }

  suspend fun updateFavorite(breedId: Long, favorite: Boolean) {
    log.i { "Breed $breedId: Favorited $favorite" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.tableQueries.updateFavorite(favorite, breedId)
    }
  }

  fun selectAllProfile(): FLow<UserProfile> =
    dbRef.userProfileQueries
  fun selectProfileUser(): Flow<UserProfile> =
    dbRef.userProfileQueries
      .selectProfile()
      .asFlow()
      .mapToOne()
      .flowOn(backgroundDispatcher)

  fun selectProfileMasterMenu(): Flow<UserProfile> =
    dbRef.userProfileQueries
      .selectMasterMenu()
      .asFlow()
      .mapToOne()
      .flowOn(backgroundDispatcher)

  fun getAuth(): Flow<Auth> =
    dbRef.authQueries
      .selectByUserId("1")
      .asFlow()
      .mapToOne()
      .flowOn(backgroundDispatcher)

  fun selectAll(): Flow<List<Auth>> =
    dbRef.authQueries
      .selectAll()
      .asFlow()
      .mapToList()
      .flowOn(backgroundDispatcher)

  fun selectById(id: Long): Flow<List<Breed>> =
    dbRef.tableQueries
      .selectById(id)
      .asFlow()
      .mapToList()
      .flowOn(backgroundDispatcher)

  suspend fun deleteAll() {
    log.i { "Database Cleared" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.tableQueries.deleteAll()
    }
  }
}
