package co.touchlab.kampkit.ktor

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken.Result as AuthResult
import co.touchlab.kampkit.response.Balance as BalanceResult
import co.touchlab.kampkit.response.MasterMenu.Result as MasterMenuResult

interface ProfileApi {
  suspend fun fetchAuth(): AuthResult
  suspend fun submitLogin(tokenSession: String, email: String, pass: String): AuthResult
  suspend fun checkAuth(tokenSession: String): AuthResult
  suspend fun submitLogout(tokenSession: String): AuthResult
  suspend fun fetchMasterMenu(): MasterMenuResult
  suspend fun fetchBalance(tokenSession: String, userId: String): BalanceResult
}