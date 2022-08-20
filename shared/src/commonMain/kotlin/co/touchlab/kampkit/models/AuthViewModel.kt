package co.touchlab.kampkit.models

import co.touchlab.kampkit.ApiConstant.OK
import co.touchlab.kampkit.db.Auth
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
  private val authRepository: AuthRepository,
  log: Logger
) : ViewModel() {
  private val log = log.withTag("AuthVM")
  private val mutableAuthState: MutableStateFlow<AuthViewState> =
    MutableStateFlow(AuthViewState(isLoading = true))
  val authState: StateFlow<AuthViewState> = mutableAuthState

  init {
    observeAuth()
  }

  override fun onCleared() {
    log.v { "Clearing AuthVM" }
  }

  private fun observeAuth() {
    val refreshFlow = flow<Throwable?> {
      try {
        authRepository.refreshAuthIfStale()
        emit(null)
      } catch (exception: Exception) {
        emit(exception)
      }
    }

    viewModelScope.launch {
      combine(refreshFlow, authRepository.getAuths()) { throwable, auths -> throwable to auths }
        .collect { (error, auths) ->
          mutableAuthState.update { previousState ->
            val errorMessage = if (error != null) {
              "Unable to download auth data"
            } else {
              previousState.error
            }
            AuthViewState(
              isLoading = false,
              auths = auths.takeIf { it.isEmpty() },
              // error = errorMessage.takeIf { auths.get(0).error_code.toInt() == OK }
              error = ""
            )
          }
        }
    }
  }

  fun getAuth(): Job {
    mutableAuthState.update {
      it.copy(isLoading = true)
    }
    return viewModelScope.launch {
      log.v { "get token" }
      try {
        authRepository.getAuth()
      } catch (exception: Exception) {
        handleAuthError(exception)
      }
    }
  }

  private fun handleAuthError(throwable: Throwable) {
    log.e(throwable) { "Error get Token Auth" }
    // mutableAuthState.update {
    //   if (it.auths?.get(0)?.error_code?.toInt() != OK) {
    //     AuthViewState(error = "Unable to get Auth Token, check network")
    //   } else {
    //     it.copy(isLoading = false)
    //   }
    // }
  }
}

data class AuthViewState(
  val auths: List<Auth>? = null,
  val error: String? = null,
  val isLoading: Boolean = false
)