package co.touchlab.kampkit.models

import co.touchlab.kampkit.db.Profile
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        // repository.refreshMasterMenuIfStale()
        emit(null)
      } catch (e: Exception) {
        emit(e)
      }
    }

    viewModelScope.launch {
      combine(
        refreshFlow,
        repository.selectAuth()
      ) { throwable, auth ->
        throwable to auth
      }.collect { (error, profile) ->
        log.e { "${profile.toString()}" }
        mutableState.update { previousState ->
          val errorMessage = if (error != null) {
            "Unable to download profiles"
          } else {
            previousState.error
          }

          ProfileState(
            isLoading = false,
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
      log.v { "refresh profileauth" }
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
      if (it.masterMenu.payload.isBlank())
        ProfileState(errorMasterMenu = "Unable load Master Menu")
      else it.copy(isLoadingMasterMenu = false)
    }
  }

  private fun handleError(throwable: Throwable) {
    log.e(throwable) { "Error on PROFILE" }
    mutableState.update {
      if (it.all.type.isBlank()) {
        ProfileState(error = "Unable to refresh profiles")
      } else {
        it.copy(isLoading = false)
      }
    }
  }
}

data class ProfileState(
  val all: Profile = Profile("AUTH", "x", ""),
  val masterMenu: Profile = Profile("MASTER_MENU", "", ""),
  val isLoading: Boolean = false,
  val isLoadingMasterMenu: Boolean = false,
  val error: String = "",
  val errorMasterMenu: String = ""
)