package otto.com.sdk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import otto.com.sdk.ui.MainScreen
import otto.com.sdk.ui.sortByRank
import com.otto.sdk.shared.kampkit.android.ui.theme.KaMPKitTheme
import io.sentry.ISpan
import io.sentry.ITransaction
import io.sentry.Sentry
import otto.com.sdk.ui.data.MenuItem

class MainFragment : Fragment() {

  var itemsModifier: (List<MenuItem>) -> List<MenuItem> = ::sortByRank

  companion object {
    fun newInstance(
      itemsModifier: (items: List<MenuItem>) -> List<MenuItem>
    ): MainFragment {
      return MainFragment().apply {
        this.itemsModifier = itemsModifier
      }
    }

    fun newInstance(): MainFragment {
      return MainFragment()
    }
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
    // itemsModifier = ::sortByRankDescend
    return ComposeView(requireContext()).apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent {
        KaMPKitTheme {
          MainScreen(
            // TODO: default sort = @see Composables.kt

            // TODO: pre-defined sort function
            // fSort = itemsModifier,
            fy = itemsModifier,
            // fSort = ::sortByRankDescend,
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