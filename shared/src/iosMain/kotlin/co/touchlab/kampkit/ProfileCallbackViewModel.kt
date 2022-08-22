package co.touchlab.kampkit

import co.touchlab.kampkit.models.CallbackViewModel
import co.touchlab.kampkit.models.ProfileRepository
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kermit.Logger

@Suppress("Unused")
class ProfileCallbackViewModel(
  profileRepository: ProfileRepository,
  log: Logger
) : CallbackViewModel() {
  override val viewModel = ProfileViewModel(profileRepository, log)
  val profile = viewModel.state.asCallbacks()
  fun refreshProfile() {
    viewModel.refreshProfileAuth()
  }

  fun refreshMasterMenu() {
    viewModel.refreshMasterMenu()
  }
}