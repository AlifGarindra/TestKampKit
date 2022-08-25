package co.touchlab.kampkit.android.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import co.touchlab.kampkit.android.ui.data.PpobMenuModel
import co.touchlab.kampkit.models.ProfileState
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kampkit.response.MasterMenu
import co.touchlab.kermit.Logger
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.getViewModel
import org.koin.core.Koin
import org.koin.core.context.GlobalContext.get
import otto.com.sdk.BuildConfig
import otto.com.sdk.R
import otto.com.sdk.SDKManager

fun sortByName(items: List<MasterMenu.Item>): List<MasterMenu.Item> = items.sortedBy { it.name }
fun sortByNameDescend(items: List<MasterMenu.Item>): List<MasterMenu.Item> =
  items.sortedByDescending { it.name }

fun sortByRank(items: List<MasterMenu.Item>): List<MasterMenu.Item> = items.sortedBy { it.rank }
fun sortByRankDescend(items: List<MasterMenu.Item>): List<MasterMenu.Item> =
  items.sortedByDescending { it.rank }

@Composable
fun MainScreen(
  profileViewModel: ProfileViewModel = getViewModel(),
  fSort: (List<MasterMenu.Item>) -> List<MasterMenu.Item> = ::sortByRank
) {
  val lifecycleOwner = LocalLifecycleOwner.current

  val lifecycleAwareProfileFlow =
    remember(profileViewModel.state, lifecycleOwner) {
      profileViewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

  @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
  // val authState by lifeCycleAwareAuthFlow.collectAsState(authViewModel.authState.value)
  val profileState by lifecycleAwareProfileFlow.collectAsState(profileViewModel.state.value)

  val sdk = SDKManager.getInstance(LocalContext.current)

  MainScreenContent(
    {
      profileViewModel.refreshProfileAuth()
      // if (profileState.masterMenu.payload.isNullOrBlank())
      profileViewModel.refreshMasterMenu()
      sdk.changeTheX("${System.currentTimeMillis()}")
      println(sdk.x)
    },
    fSort,
    profileState,
  )
}

class Ref(var value: Int)

@Composable
inline fun LogCompositions(tag: String, msg: String) {
  if (BuildConfig.DEBUG) {
    val ref = remember { Ref(0) }
    SideEffect { ref.value++ }
    Log.d(tag, "Compositions: [$msg] ${ref.value}")
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
  onRefresh: () -> Unit = {},
  doSort: (List<MasterMenu.Item>) -> List<MasterMenu.Item>,
  profileState: ProfileState

) {
  Surface(
    color = MaterialTheme.colors.background,
    modifier = Modifier.fillMaxSize()
  ) {
    SwipeRefresh(
      state = rememberSwipeRefreshState(isRefreshing = profileState.isLoading),
      onRefresh = onRefresh
    ) {
      Column(modifier = Modifier) {
        val listOfMenu: ArrayList<PpobMenuModel> = getListOfMenu()
        GridMenuView(listOfMenu)

        val masterMenu = profileState.masterMenu
        if (masterMenu.isNotEmpty()) {
          Success(successData = masterMenu, doSort = doSort)
        }
      }
    }
  }
}

@Composable
fun RowSortButton() {
}

@Composable
fun Empty(json: String) {
  LogCompositions("FDLN", json)
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(json)
  }
}

@Composable
fun Error(error: String) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = error)
  }
}

@Composable
fun Success(
  successData: List<MasterMenu.Item>,
  doSort: (List<MasterMenu.Item>) -> List<MasterMenu.Item>
) {
  MenuList(items = doSort(successData))
}

@Composable
fun MenuList(items: List<MasterMenu.Item>) {
  LazyColumn {
    items(items) { item ->
      MenuRow(item = item, )
      Divider()
    }
  }
}

@Composable
fun MenuRow(item: MasterMenu.Item) {
  Row(Modifier.padding(10.dp)) {
    Text("[ ${item.rank} ] ${item.name}")
  }
}

@Composable
fun getListOfMenu(): ArrayList<PpobMenuModel> {
  val menuModelArrayList: ArrayList<PpobMenuModel> = ArrayList()
  menuModelArrayList.add(
    PpobMenuModel("Telepon & Air", R.drawable.ic_isimple_telepon, 5, "www.google.com")
  )
  menuModelArrayList.add(
    PpobMenuModel("Listrik", R.drawable.ic_isimple_listrik, 2, "www.facebook.com")
  )
  menuModelArrayList.add(
    PpobMenuModel("Voucher Game", R.drawable.ic_isimple_game, 1, "www.weekendinc.com")
  )
  menuModelArrayList.add(
    PpobMenuModel("Pinjaman", R.drawable.ic_isimple_pinjaman, 6, "www.youtube.com")
  )
  menuModelArrayList.add(
    PpobMenuModel("BPJS Kesehatan", R.drawable.ic_isimple_bpjs, 3, "www.github.com")
  )
  menuModelArrayList.add(
    PpobMenuModel("Asuransi", R.drawable.ic_isimple_asuransi, 4, "www.stackoverflow.com")
  )
  menuModelArrayList.add(
    PpobMenuModel("E-Wallet", R.drawable.ic_isimple_wallet, 7, "www.medium.com")
  )
  return menuModelArrayList
}