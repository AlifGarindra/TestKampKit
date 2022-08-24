package otto.com.sdk.ui

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
import co.touchlab.kampkit.injectLogger
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kermit.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import otto.com.sdk.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment()  {
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null

  // private val log: Logger by injectLogger("MainActivity")
  // private val profileViewModel: ProfileViewModel by viewModel<ProfileViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_menu, container, false)

    // return ComposeView(requireContext()).apply {
    //   setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    //   setContent {
    //     KaMPKitTheme {
    //       MainScreen(
    //         profileViewModel,
    //         // TODO: default sort = @see Composables.kt
    //
    //         // TODO: pre-defined sort fuction
    //         fSort = ::sortByRankDescend
    //
    //         // TODO: custom sort function
    //         // fSort = { items ->
    //         //   val newItems = arrayListOf<MasterMenu.Item>()
    //         //   val emas: MasterMenu.Item? = items.find { it.code == "emas" }
    //         //   emas?.let { newItems.add(it) }
    //         //   newItems
    //         // }
    //       )
    //     }
    //   }
    // }
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
      MenuFragment().apply {
        arguments = Bundle().apply {
          putString(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }
}