package com.otto.sdk.shared.kampkit

import com.otto.sdk.shared.kampkit.db.Breed
import com.otto.sdk.shared.models.BreedRepository
import com.otto.sdk.shared.models.BreedViewModel
import com.otto.sdk.shared.models.CallbackViewModel
import co.touchlab.kermit.Logger

@Suppress("Unused") // Members are called from Swift
class BreedCallbackViewModel(
    breedRepository: BreedRepository,
    log: Logger
) : CallbackViewModel() {

    override val viewModel = BreedViewModel(breedRepository, log)

    val breeds = viewModel.breedState.asCallbacks()

    fun refreshBreeds() {
        viewModel.refreshBreeds()
    }

    fun updateBreedFavorite(breed: Breed) {
        viewModel.updateBreedFavorite(breed)
    }
}
