package com.otto.sdk.shared.models

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import com.otto.sdk.shared.response.MasterMenu
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ProfileViewModel(
  private val repository: ProfileRepository,
  log: Logger
) : ViewModel() {
  private val log = log.withTag("ProfileViewModel")
  private val mutableState: MutableStateFlow<ProfileState> =
    MutableStateFlow(
      ProfileState(
        isLoading = true,
        isLoadingMasterMenu = true
      )
    )

  val state: StateFlow<ProfileState> = mutableState

  init {
    observeState()
  }

  override fun onCleared() {
    log.v { "Clearing UserProfileViewModel" }
  }

  private fun observeState() {
    val refreshFlow = flow<Throwable?> {
      try {
        repository.refreshAuthIfStale()
        repository.refreshMasterMenuIfStale()
        emit(null)
      } catch (e: Exception) {
        // emit(e)
      }
    }

    viewModelScope.launch {
      combine(
        refreshFlow,
        repository.selectAuth(),
        repository.selectMasterMenu(),
      ) { throwable, auth, masterMenu ->
        throwable to mapOf("auth" to auth, "masterMenu" to masterMenu)
      }.collect { (error, res) ->
        mutableState.update { prevState ->
          val errorMessage = if (error != null) {
            "Unable to download profiles"
          } else {
            prevState.error
          }
          val auth: AuthToken.Auth = if (res["auth"] != null) {
            val jsonAuth: String = res["auth"]?.payload ?: "[]"
            val resAuth: AuthToken.Result = Json.decodeFromString(jsonAuth)
            resAuth.data
          } else AuthToken.Auth()
          val masterMenu: List<MasterMenu.Item> = if (res["masterMenu"] != null) {
            val jsonMasterMenu: String = res["masterMenu"]?.payload ?: "[]"
            val resMasterMenu: MasterMenu.Result = Json.decodeFromString(jsonMasterMenu)
            resMasterMenu.features.filter { it.key == "QzzEp5g29fFymxeMX27vc2W9mZ2Qab" }[0].item
          } else arrayListOf()

          prevState.copy(
            isLoading = false,
            isLoadingMasterMenu = false,
            auth = auth,
            masterMenu = masterMenu,
            error = errorMessage
          )
        }
      }
    }
  }

  fun refreshProfileAuth(): Job {
    mutableState.update {
      it.copy(isLoading = true)
    }
    return viewModelScope.launch {
      log.e { "refresh profileauth" }
      try {
        repository.fetchAuth()
      } catch (e: Exception) {
        handleError(e)
      }
    }
  }

  fun refreshMasterMenu(): Job {
    mutableState.update {
      it.copy(isLoadingMasterMenu = true)
    }
    return viewModelScope.launch {
      try {
        repository.fetchMasterMenu()
      } catch (e: Exception) {
        handleErrorMasterMenu(e)
      }
    }
  }

  private fun handleErrorMasterMenu(throwable: Throwable) {
    mutableState.update {
      if (it.masterMenu.isEmpty())
        ProfileState(errorMasterMenu = "Unable load Master Menu")
      else it.copy(isLoadingMasterMenu = false)
    }
  }

  private fun handleError(throwable: Throwable) {
    log.e(throwable) { "Error on PROFILE" }
    mutableState.update {
      if (it.auth.tokenCode.isBlank()) {
        ProfileState(error = "Unable to refresh profiles")
      } else {
        it.copy(isLoading = false)
      }
    }
  }
}

data class ProfileState(
  val auth: AuthToken.Auth = AuthToken.Auth(),
  val masterMenu: List<MasterMenu.Item> = arrayListOf(),
  val isLoading: Boolean = false,
  val isLoadingMasterMenu: Boolean = false,
  val error: String = "",
  val errorMasterMenu: String = ""
)