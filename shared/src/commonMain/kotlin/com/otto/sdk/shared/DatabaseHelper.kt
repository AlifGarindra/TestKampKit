package com.otto.sdk.shared

import com.otto.sdk.shared.kampkit.db.KaMPKitDb
import com.otto.sdk.shared.sqldelight.transactionWithContext
import co.touchlab.kermit.Logger
import com.otto.sdk.shared.db.Breed
import com.otto.sdk.shared.db.Profile
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

  suspend fun insertBreeds(breeds: List<String>) {
    log.d { "Inserting ${breeds.size} breeds into database" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      breeds.forEach { breed ->
        dbRef.tableQueries.insertBreed(breed)
      }
    }
  }

  fun selectProfileAuth(): Flow<Profile> =
    dbRef.profileQueries
      .selectAuth()
      .asFlow()
      .mapToOne()
      .flowOn(backgroundDispatcher)

  suspend fun updateProfileAuth(json: String) {
    log.i { "Profile $json" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.profileQueries.updateAuth(json)
    }
  }

  suspend fun updateProfile(json: String) {
    log.i { "Profile update $json" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.profileQueries.updateProfile(json)
    }
  }

  suspend fun updateBalance(json: String) {
    log.i { "Balance update ${json}" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.profileQueries.updateSaldo(json)
    }
  }

  suspend fun updateMasterMenu(json: String): Unit =
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.profileQueries.updateMasterMenu(json)
    }

  suspend fun updateFavorite(breedId: Long, favorite: Boolean) {
    log.i { "Breed $breedId: Favorited $favorite" }
    dbRef.transactionWithContext(backgroundDispatcher) {
      dbRef.tableQueries.updateFavorite(favorite, breedId)
    }
  }

  fun selectAllProfile(): Flow<List<Profile>> =
    dbRef.profileQueries
      .selectAll()
      .asFlow()
      .mapToList()
      .flowOn(backgroundDispatcher)

  fun selectProfileUser(): Flow<Profile> =
    dbRef.profileQueries
      .selectProfile()
      .asFlow()
      .mapToOne()
      .flowOn(backgroundDispatcher)

  fun selectProfileMasterMenu(): Flow<Profile> =
    dbRef.profileQueries
      .selectMasterMenu()
      .asFlow()
      .mapToOne()
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
