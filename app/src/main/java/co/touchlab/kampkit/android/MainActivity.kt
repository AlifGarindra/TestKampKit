package co.touchlab.kampkit.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import co.touchlab.kampkit.android.ui.MainScreen
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme
import co.touchlab.kampkit.injectLogger
import co.touchlab.kampkit.models.AuthViewModel
import co.touchlab.kampkit.models.BreedViewModel
import co.touchlab.kampkit.models.ProductMenuViewModel
import co.touchlab.kermit.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

  private val log: Logger by injectLogger("MainActivity")
  private val viewModel: BreedViewModel by viewModel<BreedViewModel>()
  private val productMenuViewModel: ProductMenuViewModel by viewModel<ProductMenuViewModel>()
  private val authViewModel: AuthViewModel by viewModel<AuthViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      KaMPKitTheme {
        MainScreen(viewModel, productMenuViewModel, authViewModel, log)
      }
    }
  }
}
