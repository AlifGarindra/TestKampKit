package otto.com.sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import co.touchlab.kampkit.android.ui.MainScreen
import co.touchlab.kampkit.android.ui.sortByRankDescend
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme
import co.touchlab.kampkit.injectLogger
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kermit.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

  private val log: Logger by injectLogger("MainActivity")
  private val profileViewModel: ProfileViewModel by viewModel<ProfileViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      KaMPKitTheme {
        MainScreen(
          profileViewModel,
          log,
          // TODO: default sort = @see Composables.kt

          // TODO: pre-defined sort fuction
          fSort = ::sortByRankDescend

          // TODO: custom sort function
          // fSort = { items ->
          //   val newItems = arrayListOf<MasterMenu.Item>()
          //   val emas: MasterMenu.Item? = items.find { it.code == "emas" }
          //   emas?.let { newItems.add(it) }
          //   newItems
          // }
        )
      }
    }
  }
}
