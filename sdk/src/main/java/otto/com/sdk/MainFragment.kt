package otto.com.sdk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.touchlab.kampkit.android.ui.MainScreen
import co.touchlab.kampkit.android.ui.sortByRankDescend
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme
import co.touchlab.kampkit.response.MasterMenu

class MainFragment : Fragment() {

  companion object {
    fun newInstance() = MainFragment()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // return inflater.inflate(R.layout.fragment_menu, container, false)

    return ComposeView(requireContext()).apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent {
        KaMPKitTheme {
          MainScreen(
            // TODO: default sort = @see Composables.kt

            // TODO: pre-defined sort fuction
            // fSort = ::sortByRankDescend

            // TODO: custom sort function
            fSort = { items ->
              val newItems = arrayListOf<MasterMenu.Item>()
              val emas: MasterMenu.Item? = items.find { it.code == "emas" }
              emas?.let { newItems.add(it) }
              newItems
            }
          )
        }
      }
    }
  }
}