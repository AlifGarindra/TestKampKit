package co.touchlab.kampkit.ktor

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken.Result
import co.touchlab.kampkit.response.Balance
import co.touchlab.kampkit.response.MasterMenu

interface UserProfileApi {
  suspend fun fetchAuth(): Result
  suspend fun submitLogin(tokenSession: String, email: String, pass: String): Result
  suspend fun checkAuth(tokenSession: String): Result
  suspend fun submitLogout(tokenSession: String): Result
  suspend fun fetchMasterMenu(): MasterMenu
  suspend fun fetchBalance(tokenSession: String, userId: String): Balance
}