package com.hasanalic.ecommerce.feature_home.presentation.favorite_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCases: FavoriteUseCases,
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private val _favoriteState = MutableLiveData(FavoriteState())
    var favoriteState: LiveData<FavoriteState> = _favoriteState

    fun getUserFavoriteProductsIfUserLoggedIn() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        if (userId == null) {
            _favoriteState.value = _favoriteState.value?.copy(
                isUserLoggedIn = false,
                isLoading = false
            )
            return
        }

        getUserFavoriteProducts(userId)
    }

    private fun getUserFavoriteProducts(userId: String) {
        _favoriteState.value = FavoriteState(isLoading = true)
        viewModelScope.launch {
            when (val result = favoriteUseCases.getFavoriteProductsUseCase(userId)) {
                is Result.Error -> handleGetFavoriteProductsError(result.error)
                is Result.Success -> _favoriteState.value = FavoriteState(
                    favoriteProductList = result.data.toMutableList(),
                    userId = userId
                )
            }
        }
    }

    private fun handleGetFavoriteProductsError(error: DataError.Local) {
        when (error) {
            DataError.Local.NOT_FOUND -> {
                _favoriteState.value = FavoriteState(
                    dataError = "You don't have a favorite product."
                )
            }
            DataError.Local.UNKNOWN -> {
                _favoriteState.value = FavoriteState(
                    dataError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun removeProductFromFavorites(productId: String) {
        viewModelScope.launch {
            val userId = _favoriteState.value!!.userId!!
            when (val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
                is Result.Error -> handleDeleteFavoriteError(result.error)
                is Result.Success -> removeProductFromMutableFavoriteProductList(productId)
            }
        }
    }

    private fun handleDeleteFavoriteError(error: DataError.Local) {
        when (error) {
            DataError.Local.DELETION_FAILED -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "The product could not be removed from favorites."
                )
            }
            DataError.Local.UNKNOWN -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "For an unknown reason, the product could not be removed from favorites."
                )
            }
            else -> {}
        }
    }

    private fun removeProductFromMutableFavoriteProductList(productId: String) {
        val currentFavorites = _favoriteState.value!!.favoriteProductList

        currentFavorites.removeAll { it.productId == productId }

        _favoriteState.value = _favoriteState.value!!.copy(
            favoriteProductList = currentFavorites
        )
    }

    fun addProductToCart(productId: String) {
        viewModelScope.launch {
            val userId = _favoriteState.value!!.userId!!
            val shoppingCartEntity = ShoppingCartItemsEntity(userId, productId, 1)
            when (val result = shoppingCartUseCases.insertShoppingCartItemEntityUseCase(shoppingCartEntity)) {
                is Result.Error -> handleAddProductToCartError(result.error)
                is Result.Success -> setAddedToCart(productId, true)
            }
        }
    }

    private fun handleAddProductToCartError(error: DataError.Local) {
        when (error) {
            DataError.Local.INSERTION_FAILED -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "The product could not be added to the shopping cart."
                )
            }

            DataError.Local.UNKNOWN -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "The product could not be added to the shopping cart."
                )
            }

            else -> {}
        }
    }

    fun removeProductFromCart(productId: String) {
        viewModelScope.launch {
            val userId = _favoriteState.value!!.userId!!
            when (val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromCartError(result.error)
                is Result.Success -> setAddedToCart(productId, false)
            }
        }
    }

    private fun handleRemoveProductFromCartError(error: DataError.Local) {
        when (error) {
            DataError.Local.DELETION_FAILED -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "The product could not be removed from the cart."
                )
            }

            DataError.Local.UNKNOWN -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }

            else -> {}
        }
    }

    private fun setAddedToCart(productId: String, isAddedToCart: Boolean) {
        val currentFavoriteProductList = _favoriteState.value!!.favoriteProductList

        val updatedFavoriteProductList = currentFavoriteProductList.map { product ->
            if (product.productId == productId) {
                product.copy(addedToShoppingCart = isAddedToCart)
            } else {
                product
            }
        }

        _favoriteState.value = _favoriteState.value!!.copy(
            favoriteProductList = updatedFavoriteProductList.toMutableList()
        )
    }
}