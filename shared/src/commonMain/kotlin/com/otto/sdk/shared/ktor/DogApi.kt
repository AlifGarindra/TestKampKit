package com.otto.sdk.shared.ktor

import com.otto.sdk.shared.response.BreedResult

interface DogApi {
    suspend fun getJsonFromApi(): BreedResult
}
