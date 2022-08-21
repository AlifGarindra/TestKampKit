package co.touchlab.kampkit.models

import co.touchlab.kampkit.db.UserProfile
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
  private val repository: UserProfileRepository,
  log: Logger
) : ViewModel() {
  private val log = log.withTag("UserProfileViewModel")
  private val mutableState: MutableStateFlow<UserProfileState> =
    MutableStateFlow(UserProfileState(isLoading = true))
  val state: StateFlow<UserProfileState> = mutableState

  init {
  }

  override fun onCleared() {
    log.v { "Clearing UserProfileViewModel" }
  }

  private fun observeState() {
    val refreshFlow = flow<Throwable?> {
      try {
        repository.refreshAuthIfStale()
        emit(null)
      } catch (e: Exception) {
        emit(e)
      }
    }

    viewModelScope.launch {
      combine(refreshFlow, repository.selectAll()) { throwable, profiles ->
        throwable to profiles
      }.collect { (error, profiles) ->
        mutableState.update { previousState ->
          val errorMessage = if (error != null) {
            "Unable to download profiles"
          } else {
            previousState.error
          }
          UserProfileState(
            isLoading = false,
            all = profiles,
            error = errorMessage
          )
        }
      }
    }
  }
}

data class UserProfileState(
  val all: List<UserProfile> = arrayListOf<UserProfile>(),
  val isLoading: Boolean = false,
  val error: String = ""
)