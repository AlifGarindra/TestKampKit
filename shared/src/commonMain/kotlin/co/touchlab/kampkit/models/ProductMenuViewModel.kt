package co.touchlab.kampkit.models

import co.touchlab.kampkit.db.ProductMenu
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductMenuViewModel(
  private val productMenuRepository: ProductMenuRepository,
  log: Logger
) : ViewModel() {
  private val log = log.withTag("ProductMenuCommonViewModel")

  private val mutableProductMenuState: MutableStateFlow<ProductMenuViewState> =
    MutableStateFlow(ProductMenuViewState(isLoading = true))

  val productMenuState: StateFlow<ProductMenuViewState> = mutableProductMenuState

  init {
    observeProductMenu()
  }

  override fun onCleared() {
    log.v { "Clearing ProductMenuViewModel" }
  }

  private fun observeProductMenu() {
    val refreshFlow = flow<Throwable?> {
      try {
        productMenuRepository.refreshProductMenuIfStale()
        emit(null)
      } catch (exception: Exception) {
        emit(exception)
      }
    }

    viewModelScope.launch {
      combine(refreshFlow, productMenuRepository.getProductMenu()) { throwable, productMenuList ->
        throwable to productMenuList
      }.collect { (error, productMenuList) ->
        mutableProductMenuState.update { previousState ->
          val errorMessage = if (error != null) {
            "Unable to download product menu list"
          } else {
            previousState.error
          }
          ProductMenuViewState(
            isLoading = false,
            productMenu = productMenuList.takeIf { it.isNotEmpty() },
            error = errorMessage.takeIf { productMenuList.isEmpty() },
            isEmpty = productMenuList.isEmpty() && errorMessage == null
          )
        }
      }
    }
  }

  fun refreshProductMenuList(): Job {
    mutableProductMenuState.update { it.copy(isLoading = true) }
    return viewModelScope.launch {
      log.v { "refreshProductMenuList" }
      try {
        productMenuRepository.refreshProductMenu()
      } catch (exception: Exception) {
        handleProductMenuError(exception)
      }
    }
  }

  private fun handleProductMenuError(throwable: Throwable) {
    log.e(throwable) { "Error download product menu list" }
    mutableProductMenuState.update {
      if (it.productMenu.isNullOrEmpty()) {
        ProductMenuViewState(error = "Unable to refresh product menu list")
      } else {
        it.copy(isLoading = false)
      }
    }
  }
}

data class ProductMenuViewState(
  val productMenu: List<ProductMenu>? = null,
  val error: String? = null,
  val isLoading: Boolean = false,
  val isEmpty: Boolean = false

)