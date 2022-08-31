package com.otto.sdk.shared.ktor

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken.Result as AuthResult
import com.otto.sdk.shared.response.Balance as BalanceResult
import com.otto.sdk.shared.response.MasterMenu.Result as MasterMenuResult

interface ProfileApi {
  suspend fun fetchAuth(): AuthResult
  suspend fun submitLogin(tokenSession: String, email: String, pass: String): AuthResult
  suspend fun checkAuth(tokenSession: String): AuthResult
  suspend fun submitLogout(tokenSession: String): AuthResult
  suspend fun fetchMasterMenu(): MasterMenuResult
  suspend fun fetchBalance(tokenSession: String, userId: String): BalanceResult
}