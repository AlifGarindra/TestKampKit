package co.touchlab.kampkit.ktor

import co.AuthTokenResulttouchlab.kampkit.response.AuthToken
import co.touchlab.kampkit.response.AuthLoginResult

interface AuthApi {
  suspend fun getToken(): AuthToken.Result
  suspend fun doLogin(): AuthLoginResult
}