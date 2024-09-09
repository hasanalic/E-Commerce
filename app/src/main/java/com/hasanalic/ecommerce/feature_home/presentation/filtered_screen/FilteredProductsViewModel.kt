package com.hasanalic.ecommerce.feature_home.presentation.filtered_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases.FilteredProductsUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilteredProductsViewModel @Inject constructor(
    private val filteredProductsUseCases: FilteredProductsUseCases,
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val favoriteUseCases: FavoriteUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _filteredProductsState = MutableLiveData(FilteredProductsState())
    val filteredProducsState: LiveData<FilteredProductsState> = _filteredProductsState

    fun checkUserId() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        userId?.let {
            _filteredProductsState.value = FilteredProductsState(userId = it)
        }
    }

    fun getProductsByKeyword(keyword: String) {
        _filteredProductsState.value = _filteredProductsState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val userId = _filteredProductsState.value!!.userId
            when(val result = filteredProductsUseCases.getProductsByKeywordUseCase(userId, keyword)) {
                is Result.Error -> handleGetProductsError(result.error)
                is Result.Success -> {
                    _filteredProductsState.value = _filteredProductsState.value!!.copy(
                        isLoading = false,
                        productList = result.data
                    )
                }
            }
        }
    }

    fun getProductsByFilter(filter: Filter) {
        _filteredProductsState.value = _filteredProductsState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val userId = _filteredProductsState.value!!.userId
            when(val result = filteredProductsUseCases.getProductsByFilterUseCase(userId, filter)) {
                is Result.Error -> handleGetProductsError(result.error)
                is Result.Success -> {
                    _filteredProductsState.value = _filteredProductsState.value!!.copy(
                        isLoading = false,
                        productList = result.data
                    )
                }
            }
        }
    }

    fun getProductsByCategory(category: String) {
        _filteredProductsState.value = _filteredProductsState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val userId = _filteredProductsState.value!!.userId
            when(val result = filteredProductsUseCases.getProductsByCategoryUseCase(userId, category)) {
                is Result.Error -> handleGetProductsError(result.error)
                is Result.Success -> {
                    _filteredProductsState.value = _filteredProductsState.value!!.copy(
                        isLoading = false,
                        productList = result.data
                    )
                }
            }
        }
    }

    private fun handleGetProductsError(error: DataError.Local) {
        val errorMessage = when(error) {
            DataError.Local.NOT_FOUND -> "The products could not be brought."
            DataError.Local.UNKNOWN -> "Due to an unknown error, the products could not be delivered."
            else -> null
        }

        _filteredProductsState.value = FilteredProductsState(
            dataError = errorMessage,
            isLoading = false
        )
    }


    fun checkIfProductAlreadyInCart(productId: String, itemIndex: Int) {
        _filteredProductsState.value = _filteredProductsState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val userId = _filteredProductsState.value!!.userId
            when(val result = shoppingCartUseCases.checkIfProductInCartUseCase(userId, productId)) {
                is Result.Error -> handleCheckProductError(result.error)
                is Result.Success -> {
                    if (result.data) {
                        setAddedToCart(itemIndex, true)
                    } else {
                        addProductToCart(userId, productId, itemIndex)
                    }
                }
            }
        }
    }

    private suspend fun addProductToCart(userId: String, productId: String, itemIndex: Int) {
        val shoppingCartEntity = ShoppingCartItemsEntity(userId, productId, 1)
        when (val result = shoppingCartUseCases.insertShoppingCartItemEntityUseCase(shoppingCartEntity)) {
            is Result.Error -> handleAddProductToCartError(result.error)
            is Result.Success -> setAddedToCart(itemIndex, true)
        }
    }

    private fun handleAddProductToCartError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.INSERTION_FAILED -> "The product could not be added to the shopping cart."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _filteredProductsState.value = _filteredProductsState.value!!.copy(
            actionError = message,
            isLoading = false
        )
    }

    fun checkIfProductNotInCart(productId: String, itemIndex: Int) {
        _filteredProductsState.value = _filteredProductsState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val userId = _filteredProductsState.value!!.userId
            when(val result = shoppingCartUseCases.checkIfProductInCartUseCase(userId, productId)) {
                is Result.Error -> handleCheckProductError(result.error)
                is Result.Success -> {
                    if (result.data) {
                        removeProductFromCart(userId, productId, itemIndex)
                    } else {
                        setAddedToCart(itemIndex, false)
                    }
                }
            }
        }
    }

    private suspend fun removeProductFromCart(userId: String, productId: String, itemIndex: Int) {
        when(val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
            is Result.Error -> handleRemoveProductFromCartError(result.error)
            is Result.Success -> { setAddedToCart(itemIndex, false) }
        }
    }

    private fun handleRemoveProductFromCartError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.DELETION_FAILED -> "The item could not be deleted from the shopping cart."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }

        _filteredProductsState.value = _filteredProductsState.value!!.copy(
            actionError = message,
            isLoading = false
        )
    }

    private fun setAddedToCart(itemIndex: Int, isAddedToCart: Boolean) {
        val currentProductList = _filteredProductsState.value!!.productList
        currentProductList[itemIndex].addedToShoppingCart = isAddedToCart

        _filteredProductsState.value = _filteredProductsState.value!!.copy(
            productList = currentProductList,
            isLoading = false
        )
    }


    fun addProductToFavoritesIfUserAuthenticated(productId: String, itemIndex: Int) {
        val userId = _filteredProductsState.value!!.userId

        if (userId == ANOMIM_USER_ID) {
            _filteredProductsState.value = _filteredProductsState.value!!.copy(
                shouldUserMoveToAuthActivity = true
            )
            return
        }

        viewModelScope.launch {
            checkIfProductAlreadyInFavorites(userId, productId, itemIndex)
        }
    }

    private suspend fun checkIfProductAlreadyInFavorites(userId: String, productId: String, itemIndex: Int) {
        _filteredProductsState.value = _filteredProductsState.value!!.copy(isLoading = true)
        when(val result = favoriteUseCases.checkIfProductInFavoritesUseCase(userId, productId)) {
            is Result.Error -> handleCheckProductError(result.error)
            is Result.Success -> {
                if (result.data) {
                    setAddedToFavorites(itemIndex, true)
                } else {
                    addProductToFavorites(userId, productId, itemIndex)
                }
            }
        }
    }

    private suspend fun addProductToFavorites(userId: String, productId: String, itemIndex: Int) {
        val favoriteEntity = FavoritesEntity(userId, productId)
        when(val result = favoriteUseCases.insertFavoriteAndGetIdUseCase(favoriteEntity)) {
            is Result.Error -> handleAddProductToFavoritesError(result.error)
            is Result.Success -> setAddedToFavorites(itemIndex, true)
        }
    }

    private fun handleAddProductToFavoritesError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.INSERTION_FAILED -> "The product could not be added to favorites."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _filteredProductsState.value = _filteredProductsState.value!!.copy(
            actionError = message,
            isLoading = false
        )
    }

    fun removeProductFromFavoritesIfUserAuthenticated(productId: String, itemIndex: Int) {
        val userId = _filteredProductsState.value!!.userId
        if (userId == ANOMIM_USER_ID) {
            _filteredProductsState.value = _filteredProductsState.value!!.copy(
                shouldUserMoveToAuthActivity = true
            )
            return
        }

        viewModelScope.launch {
            checkIfProductNotInFavorites(userId, productId, itemIndex)
        }
    }

    private suspend fun checkIfProductNotInFavorites(userId: String, productId: String, itemIndex: Int) {
        when(val result = favoriteUseCases.checkIfProductInFavoritesUseCase(userId, productId)) {
            is Result.Error -> handleCheckProductError(result.error)
            is Result.Success -> {
                if (result.data) {
                    removeProductFromFavorites(userId, productId, itemIndex)
                } else {
                    setAddedToFavorites(itemIndex, false)
                }
            }
        }
    }

    private suspend fun removeProductFromFavorites(userId: String, productId: String, itemIndex: Int) {
        when(val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
            is Result.Error -> handleRemoveProductFromFavoritesError(result.error)
            is Result.Success -> { setAddedToFavorites(itemIndex, false) }
        }
    }

    private fun handleRemoveProductFromFavoritesError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.DELETION_FAILED -> "The product could not be removed from favorites."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _filteredProductsState.value = _filteredProductsState.value!!.copy(
            actionError = message,
            isLoading = false
        )
    }

    private fun setAddedToFavorites(itemIndex: Int, isAddedToFavorites: Boolean) {
        val currentProductList = _filteredProductsState.value!!.productList
        currentProductList[itemIndex].addedToFavorites = isAddedToFavorites

        _filteredProductsState.value = _filteredProductsState.value!!.copy(
            productList = currentProductList,
            isLoading = false
        )
    }

    private fun handleCheckProductError(error: DataError.Local) {
        val errorMessage = when(error) {
            DataError.Local.NOT_FOUND -> { "Your transaction could not be completed." }
            DataError.Local.UNKNOWN -> { "Due to an unknown error, the products could not be delivered." }
            else -> null
        }

        _filteredProductsState.value = _filteredProductsState.value!!.copy(
            actionError = errorMessage
        )
    }

    fun resetFilteredProductState() {
        _filteredProductsState.value = FilteredProductsState()
    }
}