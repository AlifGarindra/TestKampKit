package otto.com.sdk.ui

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import co.touchlab.kampkit.android.ui.MainScreen
import co.touchlab.kampkit.android.ui.sortByRankDescend
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme
import co.touchlab.kampkit.injectLogger
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kermit.Logger
import org.koin.core.component.KoinComponent

/**
 * TODO: document your custom view class.
 */
class MenuViewGroup /*@JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle), KoinComponent {

  private val log: Logger by injectLogger("MainActivity")

  // private val profileViewModel: ProfileViewModel by viewModel<ProfileViewModel>()
  init {
    orientation = VERTICAL
    val profileViewModel = ViewModelProvider.AndroidViewModelFactory(context as Application)
      .create(ProfileViewModel::class.java)
    val composeView = ComposeView(context).apply {
      setContent {
        KaMPKitTheme {
          MainScreen(
            profileViewModel,
            // log,
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
    addView(composeView)
  }
}*/