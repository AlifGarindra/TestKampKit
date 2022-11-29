package com.otto.sdk.shared

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import com.otto.sdk.shared.ktor.DogApi
import com.otto.sdk.shared.ktor.DogApiImpl
import com.otto.sdk.shared.ktor.PostApi
import com.otto.sdk.shared.ktor.PostsApiImpl
import com.otto.sdk.shared.ktor.PpobApi
import com.otto.sdk.shared.ktor.PpobApiImpl
import com.otto.sdk.shared.ktor.ProfileApi
import com.otto.sdk.shared.ktor.ProfileApiImpl
import com.otto.sdk.shared.models.BreedRepository
import com.otto.sdk.shared.models.PostRepository
import com.otto.sdk.shared.models.PpobRepository
import com.otto.sdk.shared.models.ProfileRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun initKoin(appModule: Module): KoinApplication {
  val koinApplication = startKoin {
    modules(
      appModule,
      platformModule,
      coreModule
    )
  }

  // Dummy initialization logic, making use of appModule declarations for demonstration purposes.
  val koin = koinApplication.koin
  // doOnStartup is a lambda which is implemented in Swift on iOS side
  val doOnStartup = koin.get<() -> Unit>()
  doOnStartup.invoke()

  val kermit = koin.get<Logger> { parametersOf(null) }
  // AppInfo is a Kotlin interface with separate Android and iOS implementations
  val appInfo = koin.get<AppInfo>()
  kermit.v { "App Id ${appInfo.appId}" }

  return koinApplication
}

private val coreModule = module {
  single {
    DatabaseHelper(
      get(),
      getWith("DatabaseHelper"),
      Dispatchers.Default
    )
  }

  single<HttpClient> {
    HttpClient(get()) {
      expectSuccess = true
      install(ContentNegotiation) {
        json()
      }
      install(Logging) {
        logger = object : io.ktor.client.plugins.logging.Logger {
          override fun log(message: String) {
            // log.v { message }
          }
        }

        level = LogLevel.INFO
      }
      install(HttpTimeout) {
        val timeout = 30000L
        connectTimeoutMillis = timeout
        requestTimeoutMillis = timeout
        socketTimeoutMillis = timeout
      }
    }
  }

  single<DogApi> { DogApiImpl(getWith("DogApiImpl"), get()) }
  single<ProfileApi> { ProfileApiImpl(getWith("UserProfileApiImpl"), get()) }
  single<PostApi> { PostsApiImpl(getWith("PostsApiImpl"),get()) }
  single<PpobApi> {PpobApiImpl(getWith("PpobApiImpl"),get())}
  single<Clock> { Clock.System }

  // platformLogWriter() is a relatively simple config option, useful for local debugging. For production
  // uses you *may* want to have a more robust configuration from the native platform. In KaMP Kit,
  // that would likely go into platformModule expect/actual.
  // See https://github.com/touchlab/Kermit
  val baseLogger =
    Logger(config = StaticConfig(logWriterList = listOf(platformLogWriter())), "KampKit")
  factory { (tag: String?) -> if (tag != null) baseLogger.withTag(tag) else baseLogger }

  single<BreedRepository> {
    BreedRepository(get(), get(), get(), getWith("BreedRepository"), get())
  }
  single { ProfileRepository(get(), get(), get(), getWith("ProfileRepository"), get()) }
  single { PostRepository(get()) }
  single { PpobRepository(get()) }
}

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
  return get(parameters = { parametersOf(*params) })
}

// Simple function to clean up the syntax a bit
fun KoinComponent.injectLogger(tag: String): Lazy<Logger> = inject { parametersOf(tag) }

expect val platformModule: Module
