package otto.com.sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import otto.com.sdk.ui.MainScreen
import com.otto.sdk.shared.kampkit.android.ui.theme.KaMPKitTheme
import com.otto.sdk.shared.models.ProfileViewModel
import co.touchlab.kermit.Logger
import com.otto.sdk.shared.injectLogger
import com.otto.sdk.shared.models.PostRepository
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {

  private val log: Logger by injectLogger("MainActivity")
  private val profileViewModel: ProfileViewModel by viewModel<ProfileViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      KaMPKitTheme {
        MainScreen(
          // TODO: default sort = @see Composables.kt

          // TODO: pre-defined sort fuction
          // fSort = ::sortByRankDescend

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
