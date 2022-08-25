package otto.com.sdk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import co.touchlab.kampkit.android.ui.MainScreen
import co.touchlab.kampkit.android.ui.sortByRankDescend
import co.touchlab.kampkit.android.ui.theme.KaMPKitTheme
import io.sentry.ISpan
import io.sentry.ITransaction
import io.sentry.Sentry

class MainFragment : Fragment() {

  companion object {
    fun newInstance() = MainFragment()
  }

  // override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
  //   super.onInflate(context, attrs, savedInstanceState)
  //   val styledAttributes =
  //     context?.obtainStyledAttributes(attrs, R.styleable.MainFragment_my_string)
  //   val text = styledAttributes?.getText(R.styleable.MainFragment_my_string)
  //   styledAttributes?.recycle()
  // }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // container.getAttributeResolutionStack()
    // return inflater.inflate(R.layout.fragment_menu, container, false)
    var transaction: ITransaction? = null
    var span: ISpan? = null
    // Sentry.captureMessage("testing SDK setup")
    return ComposeView(requireContext()).apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent {
        KaMPKitTheme {
          MainScreen(
            // TODO: default sort = @see Composables.kt

            // TODO: pre-defined sort function
            fSort = ::sortByRankDescend,
            onStartTrack = {
              transaction =
                Sentry.startTransaction("master-menu", "get-master-menu")
              span = transaction?.startChild("get-token")
            },
            onEndTrack = {
              span?.finish()
              transaction?.finish()
            }
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
}