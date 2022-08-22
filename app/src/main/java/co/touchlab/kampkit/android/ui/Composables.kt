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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import co.touchlab.kampkit.android.BuildConfig
import co.touchlab.kampkit.android.R
import co.touchlab.kampkit.db.Breed
import co.touchlab.kampkit.db.ProductMenu
import co.touchlab.kampkit.db.Profile
import co.touchlab.kampkit.models.BreedViewModel
import co.touchlab.kampkit.models.BreedViewState
import co.touchlab.kampkit.models.ProductMenuViewModel
import co.touchlab.kampkit.models.ProductMenuViewState
import co.touchlab.kampkit.models.ProfileState
import co.touchlab.kampkit.models.ProfileViewModel
import co.touchlab.kermit.Logger
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MainScreen(
  viewModel: BreedViewModel,
  productMenuViewModel: ProductMenuViewModel,
  profileViewModel: ProfileViewModel,
  log: Logger
) {
  val lifecycleOwner = LocalLifecycleOwner.current
  val lifecycleAwareDogsFlow = remember(viewModel.breedState, lifecycleOwner) {
    viewModel.breedState.flowWithLifecycle(lifecycleOwner.lifecycle)
  }

  val lifeCycleAwareProductMenuFlow =
    remember(productMenuViewModel.productMenuState, lifecycleOwner) {
      productMenuViewModel.productMenuState.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

  val lifecycleAwareProfileFlow =
    remember(profileViewModel.state, lifecycleOwner) {
      profileViewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

  @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
  val dogsState by lifecycleAwareDogsFlow.collectAsState(viewModel.breedState.value)
  val productMenuState by lifeCycleAwareProductMenuFlow.collectAsState(productMenuViewModel.productMenuState.value)
  // val authState by lifeCycleAwareAuthFlow.collectAsState(authViewModel.authState.value)
  val profileState by lifecycleAwareProfileFlow.collectAsState(profileViewModel.state.value)

  MainScreenContent(
    productMenuState = productMenuState,
    onProductMenuRefresh = { productMenuViewModel.refreshProductMenuList() },
    onProductMenuSuccess = { data -> log.v { "View updating with ${data?.size} product menu list" } },
    onProductMenuError = { exception -> log.e { "Displaying error $exception" } },

    dogsState = dogsState,
    onRefresh = {
      viewModel.refreshBreeds()
      productMenuViewModel.refreshProductMenuList()
      profileViewModel.refreshProfileAuth()
      // profileViewModel.refreshMasterMenu()
    },
    onSuccess = { data -> log.v { "View updating with ${data.size} breeds" } },
    onError = { exception -> log.e { "Displaying error: $exception" } },
    onFavorite = { viewModel.updateBreedFavorite(it) },
    profile = profileState
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
  productMenuState: ProductMenuViewState,
  onProductMenuRefresh: () -> Unit = {},
  onProductMenuSuccess: (List<ProductMenu>?) -> Unit = {},
  onProductMenuError: (String) -> Unit = {},

  dogsState: BreedViewState,
  onRefresh: () -> Unit = {},
  onSuccess: (List<Breed>) -> Unit = {},
  onError: (String) -> Unit = {},
  onFavorite: (Breed) -> Unit = {},
  doAuth: () -> Unit = {},
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
      Empty(json = profile.toString())
      // }
      val breeds = dogsState.breeds
      val menu = productMenuState.productMenu
      if (breeds != null && menu != null) {
        LaunchedEffect(breeds) {
          onSuccess(breeds)
        }
        LaunchedEffect(menu) {
          onProductMenuSuccess(menu)
        }
        // Success(successData = breeds, favoriteBreed = onFavorite)
        Success(successData = breeds, favoriteBreed = onFavorite, menu = menu)
      }
      // val productMenu = productMenuState.productMenu
      // LaunchedEffect(productMenu) {
      //   onProductMenuSuccess(productMenu)
      // }
      val error = dogsState.error
      if (error != null) {
        LaunchedEffect(error) {
          onError(error)
        }
        Error(error)
      }
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
  successData: List<Breed>,
  favoriteBreed: (Breed) -> Unit,
  menu: List<ProductMenu>
) {
  DogList(breeds = successData, favoriteBreed, menu = menu)
}

@Composable
fun DogList(breeds: List<Breed>, onItemClick: (Breed) -> Unit, menu: List<ProductMenu>) {
  LazyColumn {
    items(breeds) { breed ->
      DogRow(breed = breed, menu = null) {
        onItemClick(it)
      }
      Divider()
    }
  }
}

@Composable
fun DogRow(breed: Breed, menu: ProductMenu?, onClick: (Breed) -> Unit) {
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
