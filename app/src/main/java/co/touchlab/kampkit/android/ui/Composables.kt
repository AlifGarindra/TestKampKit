package co.touchlab.kampkit.android.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import co.touchlab.kampkit.android.BuildConfig
import co.touchlab.kampkit.android.R
import co.touchlab.kampkit.db.Breed
import co.touchlab.kampkit.models.BreedViewModel
import co.touchlab.kampkit.models.BreedViewState
import co.touchlab.kampkit.models.ProfileState
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kampkit.response.MasterMenu
import co.touchlab.kermit.Logger
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

fun sortByName(items: List<MasterMenu.Item>): List<MasterMenu.Item> = items.sortedBy { it.name }
fun sortByNameDescend(items: List<MasterMenu.Item>): List<MasterMenu.Item> =
  items.sortedByDescending { it.name }

fun sortByRank(items: List<MasterMenu.Item>): List<MasterMenu.Item> = items.sortedBy { it.rank }
fun sortByRankDescend(items: List<MasterMenu.Item>): List<MasterMenu.Item> =
  items.sortedByDescending { it.rank }

@Composable
fun MainScreen(
  viewModel: BreedViewModel,
  profileViewModel: ProfileViewModel,
  log: Logger,
  fSort: (List<MasterMenu.Item>) -> List<MasterMenu.Item> = ::sortByRank
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  val lifecycleAwareDogsFlow = remember(viewModel.breedState, lifecycleOwner) {
    viewModel.breedState.flowWithLifecycle(lifecycleOwner.lifecycle)
  }

  val lifecycleAwareProfileFlow =
    remember(profileViewModel.state, lifecycleOwner) {
      profileViewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

  @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
  val dogsState by lifecycleAwareDogsFlow.collectAsState(viewModel.breedState.value)
  // val authState by lifeCycleAwareAuthFlow.collectAsState(authViewModel.authState.value)
  val profileState by lifecycleAwareProfileFlow.collectAsState(profileViewModel.state.value)

  MainScreenContent(
    dogsState = dogsState,
    onRefresh = {
      viewModel.refreshBreeds()
      profileViewModel.refreshProfileAuth()
      // if (profileState.masterMenu.payload.isNullOrBlank())
      profileViewModel.refreshMasterMenu()
    },
    onSuccess = { data -> log.v { "View updating with ${data.size} breeds" } },
    onError = { exception -> log.e { "Displaying error: $exception" } },
    onFavorite = { viewModel.updateBreedFavorite(it) },
    profile = profileState,
    doSort = fSort
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

@Composable
fun MainScreenContent(
  dogsState: BreedViewState,
  onRefresh: () -> Unit = {},
  onSuccess: (List<Breed>) -> Unit = {},
  onError: (String) -> Unit = {},
  onFavorite: (Breed) -> Unit = {},
  doAuth: () -> Unit = {},
  doSort: (List<MasterMenu.Item>) -> List<MasterMenu.Item>,
  profile: ProfileState

) {
  Surface(
    color = MaterialTheme.colors.background,
    modifier = Modifier.fillMaxSize()
  ) {
    SwipeRefresh(
      state = rememberSwipeRefreshState(isRefreshing = dogsState.isLoading),
      onRefresh = onRefresh
    ) {
      // if(dogsState.isEmpty) {
      Empty(json = "${profile.auth.tokenCode} ${profile.masterMenu.size}")
      // }
      val masterMenu = profile.masterMenu
      // val breeds = dogsState.breeds
      if (masterMenu.isNotEmpty()) {
        // LaunchedEffect(breeds) {
        //   onSuccess(breeds)
        // }
        Success(successData = masterMenu, favoriteBreed = onFavorite, doSort = doSort)
      }
      // val error = dogsState.error
      // if (error != null) {
      //   LaunchedEffect(error) {
      //     onError(error)
      //   }
      //   Error(error)
      // }
    }
  }
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
    // Text(stringResource(R.string.empty_breeds))
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
  favoriteBreed: (Breed) -> Unit,
  doSort: (List<MasterMenu.Item>) -> List<MasterMenu.Item>
) {
  // DogList(breeds = successData, favoriteBreed)

  MenuList(items = doSort(successData))
}

@Composable
fun DogList(breeds: List<Breed>, onItemClick: (Breed) -> Unit) {
  LazyColumn {
    items(breeds) { breed ->
      DogRow(breed = breed) {
        onItemClick(it)
      }
      Divider()
    }
  }
}

@Composable
fun MenuList(items: List<MasterMenu.Item>) {
  LazyColumn {
    items(items) { item ->
      MenuRow(item = item)
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
fun DogRow(breed: Breed, onClick: (Breed) -> Unit) {
  Row(
    Modifier
      .clickable { onClick(breed) }
      .padding(10.dp)
  ) {
    Text(breed.name, Modifier.weight(1F))
    FavoriteIcon(breed)
  }
}

@Composable
fun FavoriteIcon(breed: Breed) {
  Crossfade(
    targetState = !breed.favorite,
    animationSpec = TweenSpec(
      durationMillis = 500,
      easing = FastOutSlowInEasing
    )
  ) { fav ->
    if (fav) {
      Image(
        painter = painterResource(id = R.drawable.ic_favorite_border_24px),
        contentDescription = stringResource(R.string.favorite_breed, breed.name)
      )
    } else {
      Image(
        painter = painterResource(id = R.drawable.ic_favorite_24px),
        contentDescription = stringResource(R.string.unfavorite_breed, breed.name)
      )
    }
  }
}
//
// @Preview
// @Composable
// fun MainScreenContentPreview_Success() {
//   MainScreenContent(
//     productMenuState = ProductMenuViewState(
//       productMenu = listOf(
//         ProductMenu(1, "pln", "listrik"),
//         ProductMenu(2, "pulsa", "telpon")
//       )
//     ),
//     dogsState = BreedViewState(
//       breeds = listOf(
//         Breed(0, "appenzeller", false),
//         Breed(1, "australian", true)
//       )
//     ),
//     profile = ProfileState(Profile("x", "xxx", ""), true, "")
//   )
// }
