package com.otto.sdk.shared.kampkit

import com.otto.sdk.shared.models.CallbackViewModel
import com.otto.sdk.shared.models.ProfileRepository
import com.otto.sdk.shared.models.ProfileViewModel
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