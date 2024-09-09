package com.hasanalic.ecommerce.feature_home.presentation.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_filter.domain.use_cases.FilterUseCases
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.HomeUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val filterUseCases: FilterUseCases,
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val favoriteUseCases: FavoriteUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _homeState = MutableLiveData(HomeState())
    val homeState: LiveData<HomeState> = _homeState

    init {
        getUserId()
        getCategories()
    }

    private fun getUserId() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        userId?.let {
            _homeState.value = _homeState.value!!.copy(userId = it)
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            when(val result = filterUseCases.getCategoriesUseCase()) {
                is Result.Error -> handleGetCategoriesError(result.error)
                is Result.Success -> {
                    _homeState.value = _homeState.value!!.copy(
                        isLoading = false,
                        categoryList = result.data.toMutableList()
                    )
                }
            }
        }
    }

    private fun handleGetCategoriesError(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _homeState.value = HomeState(
                    dataError = "Could not fetch categories."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = HomeState(
                    dataError = "Unable to fetch categories due to an unknown error."
                )
            }
            else -> {}
        }
    }

    fun getProducts() {
        _homeState.value = _homeState.value!!.copy(isLoading = true, actionError = null, dataError = null)
        viewModelScope.launch {
            val userId = _homeState.value!!.userId

            when(val result = homeUseCases.getProductsByUserIdUseCase(userId)) {
                is Result.Error -> handleGetProductsError(result.error)
                is Result.Success -> {
                    _homeState.value = homeState.value!!.copy(
                        isLoading = false,
                        productList = result.data.toMutableList()
                    )
                }
            }
        }
    }

    private fun handleGetProductsError(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _homeState.value = HomeState(
                    dataError = "The products could not be brought."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = HomeState(
                    dataError = "Due to an unknown error, the products could not be delivered."
                )
            }
            else -> {}
        }
    }

    fun removeProductFromCart(productId: String, itemIndex: Int) {
        viewModelScope.launch {
            val userId = _homeState.value!!.userId

            when(val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromCartError(result.error)
                is Result.Success -> { setAddedToCart(itemIndex, false) }
            }
        }
    }

    private fun handleRemoveProductFromCartError(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "The item could not be deleted from the shopping cart."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun addProductToCart(productId: String, itemIndex: Int) {
        viewModelScope.launch {
            val userId = _homeState.value!!.userId
            val shoppingCartEntity = ShoppingCartItemsEntity(userId, productId, 1)

            when (val result = shoppingCartUseCases.insertShoppingCartItemEntityUseCase(shoppingCartEntity)) {
                is Result.Error -> handleAddProductToCartError(result.error)
                is Result.Success -> setAddedToCart(itemIndex, true)
            }
        }
    }

    private fun handleAddProductToCartError(error: DataError.Local) {
        when(error) {
            DataError.Local.INSERTION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "The product could not be added to the shopping cart.\n"
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    private fun setAddedToCart(itemIndex: Int, isAddedToCart: Boolean) {
        val currentProductList = _homeState.value!!.productList
        currentProductList[itemIndex].addedToShoppingCart = isAddedToCart
        _homeState.value = _homeState.value!!.copy(
            productList = currentProductList, actionError = null, dataError = null
        )
    }

    fun removeProductFromFavoritesIfUserAuthenticated(productId: String, itemIndex: Int) {
        val userId = _homeState.value!!.userId

        if (userId == ANOMIM_USER_ID) {
            _homeState.value = _homeState.value!!.copy(
                shouldUserMoveToAuthActivity = true, actionError = null, dataError = null
            )
            return
        }

        viewModelScope.launch {
            when(val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromFavoritesError(result.error)
                is Result.Success -> { setAddedToFavorites(itemIndex, false) }
            }
        }
    }

    private fun handleRemoveProductFromFavoritesError(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "The product could not be removed from favorites."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun addProductToFavoritesIfUserAuthenticated(productId: String, itemIndex: Int) {
        val userId = _homeState.value!!.userId

        if (userId == ANOMIM_USER_ID) {
            _homeState.value = _homeState.value!!.copy(
                shouldUserMoveToAuthActivity = true, actionError = null, dataError = null
            )
            return
        }

        viewModelScope.launch {
            val favoriteEntity = FavoritesEntity(userId, productId)
            when(val result = favoriteUseCases.insertFavoriteAndGetIdUseCase(favoriteEntity)) {
                is Result.Error -> handleAddProductToFavoritesError(result.error)
                is Result.Success -> { setAddedToFavorites(itemIndex, true) }
            }
        }
    }

    private fun handleAddProductToFavoritesError(error: DataError.Local) {
        when(error) {
            DataError.Local.INSERTION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "The product could not be added to favorites."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    private fun setAddedToFavorites(itemIndex: Int, isAddedToFavorites: Boolean) {
        val currentProductList = _homeState.value!!.productList
        currentProductList[itemIndex].addedToFavorites = isAddedToFavorites
        _homeState.value = _homeState.value!!.copy(
            productList = currentProductList, actionError = null, dataError = null
        )
    }

    fun getProductIdByBarcode(barcode: String) {
        _homeState.value = _homeState.value!!.copy(isLoading = true, actionError = null, dataError = null)
        viewModelScope.launch {
            when(val result = homeUseCases.getProductEntityIdByBarcodeUseCase(barcode)) {
                is Result.Error -> handleGetProductIdByBarcodeError(result.error)
                is Result.Success -> {
                    _homeState.value = _homeState.value!!.copy(
                        isLoading = false,
                        scannedProductId = result.data.toString()
                    )
                }
            }
        }
    }

    private fun handleGetProductIdByBarcodeError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "No product found with this barcode."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _homeState.value = _homeState.value!!.copy(
            isLoading = false,
            actionError = message
        )
    }

    fun setScannedProductIdToNull() {
        _homeState.value = _homeState.value!!.copy(
            scannedProductId = null
        )
    }
}