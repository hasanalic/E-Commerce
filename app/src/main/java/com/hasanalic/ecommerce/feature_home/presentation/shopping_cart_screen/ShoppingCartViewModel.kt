package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.utils.toCent
import com.hasanalic.ecommerce.feature_checkout.presentation.ShoppingCartList
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_home.presentation.util.TotalCost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _shoppingCartState = MutableLiveData(ShoppingCartState())
    val shoppingCartState: LiveData<ShoppingCartState> = _shoppingCartState

    fun checkUserAndGetShoppingCartItemList() {
        _shoppingCartState.value = ShoppingCartState(isLoading = true)
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        userId?.let {
            _shoppingCartState.value = _shoppingCartState.value!!.copy(
                userId = it,
                isUserLoggedIn = true
            )
        }

        getShoppingCartItemList()
    }

    private fun getShoppingCartItemList() {
        val userId = _shoppingCartState.value!!.userId
        viewModelScope.launch {
            when(val result = shoppingCartUseCases.getProductsInShoppingCartUseCase(userId)) {
                is Result.Error -> handleGetShoppingCartItemsError(result.error)
                is Result.Success -> {
                    _shoppingCartState.value = _shoppingCartState.value!!.copy(
                        isLoading = false,
                        shoppingCartItemList = result.data.toMutableList()
                    )

                    calculateTotalPriceInShoppingCart()
                }
            }
        }
    }

    private fun handleGetShoppingCartItemsError(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _shoppingCartState.value = ShoppingCartState(
                    dataError = "Information about the products in the shopping cart could not be retrieved."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = ShoppingCartState(
                    dataError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun removeItemFromShoppingCart(productId: String) {
        val userId = _shoppingCartState.value!!.userId
        viewModelScope.launch {
            when(val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
                is Result.Error -> handleRemoveItemFromShoppingCartError(result.error)
                is Result.Success -> removeItemFromShoppingCartItemList(productId)
            }
        }
    }

    private fun handleRemoveItemFromShoppingCartError(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _shoppingCartState.value = _shoppingCartState.value!!.copy(
                    actionError = "The item could not be deleted from the shopping cart."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = _shoppingCartState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    private fun removeItemFromShoppingCartItemList(productId: String) {
        val currentShoppingCartItems = _shoppingCartState.value!!.shoppingCartItemList

        currentShoppingCartItems.removeAll { it.productId == productId }

        _shoppingCartState.value = shoppingCartState.value!!.copy(
            shoppingCartItemList = currentShoppingCartItems
        )

        calculateTotalPriceInShoppingCart()
    }

    fun increaseItemQuantityInShoppingCart(productId: String, currentQuantity: Int) {
        val userId = _shoppingCartState.value!!.userId
        viewModelScope.launch {
            val increasedItemQuantity = currentQuantity + 1
            val result = shoppingCartUseCases.updateShoppingCartItemEntityUseCase(userId, productId, increasedItemQuantity)
            when(result) {
                is Result.Error -> handleIncreaseItemQuantityError(result.error)
                is Result.Success -> increaseItemQuantityInShoppingCartItemList(increasedItemQuantity, productId)
            }
        }
    }

    private fun handleIncreaseItemQuantityError(error: DataError.Local) {
        when(error) {
            DataError.Local.UPDATE_FAILED -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "The product quantity could not be increased."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    private fun increaseItemQuantityInShoppingCartItemList(increasedItemQuantity: Int, productId: String) {
        val currentShoppingCartItemList = _shoppingCartState.value!!.shoppingCartItemList

        val updatedShoppingCartItemList = currentShoppingCartItemList.map { product ->
            if (product.productId == productId) {
                product.copy(quantity = increasedItemQuantity)
            } else {
                product
            }
        }

        _shoppingCartState.value = _shoppingCartState.value!!.copy(
            shoppingCartItemList = updatedShoppingCartItemList.toMutableList()
        )

        calculateTotalPriceInShoppingCart()
    }

    fun decreaseItemQuantityInShoppingCart(productId: String, currentQuantity: Int) {
        val userId = _shoppingCartState.value!!.userId
        val decreasedItemQuantity = currentQuantity - 1

        if (decreasedItemQuantity == 0) {
            removeItemFromShoppingCart(productId)
        } else {
            viewModelScope.launch {
                val result = shoppingCartUseCases.updateShoppingCartItemEntityUseCase(userId, productId, decreasedItemQuantity)
                when(result) {
                    is Result.Error -> handleDecreaseItemQuantityError(result.error)
                    is Result.Success -> decreaseItemQuantityInShoppingCartList(decreasedItemQuantity, productId)
                }
            }
        }
    }

    private fun handleDecreaseItemQuantityError(error: DataError.Local) {
        when(error) {
            DataError.Local.UPDATE_FAILED -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "The number of products could not be reduced."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    private fun decreaseItemQuantityInShoppingCartList(decreasedItemQuantity: Int, productId: String) {
        val currentShoppingCartItemList = _shoppingCartState.value!!.shoppingCartItemList

        val updatedShoppingCartItemList = currentShoppingCartItemList.map { product ->
            if (product.productId == productId) {
                product.copy(quantity = decreasedItemQuantity)
            } else {
                product
            }
        }

        _shoppingCartState.value = _shoppingCartState.value!!.copy(
            shoppingCartItemList = updatedShoppingCartItemList.toMutableList()
        )

        calculateTotalPriceInShoppingCart()
    }

    private fun calculateTotalPriceInShoppingCart() {
        val currentShoppingCartItemList = _shoppingCartState.value!!.shoppingCartItemList

        var totalWhole = 0
        var totalCent = 0

        for (shoppingCartItem in currentShoppingCartItemList) {
            val itemQuantity = shoppingCartItem.quantity
            val totalPriceArray = TotalCost.calculateTotalCost(shoppingCartItem.priceWhole, shoppingCartItem.priceCent, itemQuantity)

            totalWhole += totalPriceArray[0]
            totalCent += totalPriceArray[1]
        }

        val totalPriceArray = TotalCost.calculateTotalCost(totalWhole, totalCent)
        _shoppingCartState.value = _shoppingCartState.value!!.copy(
            totalPriceWhole = totalPriceArray[0],
            totalPriceCent = totalPriceArray[1]
        )
    }

    fun completeOrder() {
        _shoppingCartState.value = _shoppingCartState.value!!.copy(isLoading = true)
        val isUserLoggedIn = _shoppingCartState.value!!.isUserLoggedIn

        if (isUserLoggedIn) {
            checkShoppingCartListSize()
        } else {
            _shoppingCartState.value = _shoppingCartState.value!!.copy(
                isLoading = false,
                shouldUserMoveToAuthActivity = true
            )
        }
    }

    private fun checkShoppingCartListSize() {
        val shoppingCartListSize = _shoppingCartState.value!!.shoppingCartItemList.size

        if (shoppingCartListSize != 0) {
            saveShoppinCartListToSingleton()
            _shoppingCartState.value = _shoppingCartState.value!!.copy(
                isLoading = false,
                canUserMoveToCheckout = true
            )
        } else {
            _shoppingCartState.value = _shoppingCartState.value!!.copy(
                isLoading = false,
                actionError = "The shopping cart is empty."
            )
        }
    }

    private fun saveShoppinCartListToSingleton() {
        ShoppingCartList.shoppingCartList = _shoppingCartState.value!!.shoppingCartItemList
        ShoppingCartList.totalPrice = "${_shoppingCartState.value!!.totalPriceWhole}.${_shoppingCartState.value!!.totalPriceCent!!.toCent()}"
    }
}