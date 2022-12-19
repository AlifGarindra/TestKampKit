package com.otto.sdk.shared.models

import co.touchlab.stately.ensureNeverFrozen
import com.otto.sdk.shared.ktor.PpobApi

class PpobRepository(
  private val ppobApi: PpobApi
) {
  init {
    ensureNeverFrozen()
  }

}