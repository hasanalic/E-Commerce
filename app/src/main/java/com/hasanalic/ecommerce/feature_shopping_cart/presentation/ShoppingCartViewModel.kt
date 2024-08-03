package com.hasanalic.ecommerce.feature_shopping_cart.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.domain.use_cases.ShoppingCartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val shoppingCartUseCases: ShoppingCartUseCases
) : ViewModel() {

    private var _shoppingCartState = MutableLiveData(ShoppingCartState())
    val shoppingCartState: LiveData<ShoppingCartState> = _shoppingCartState

    fun getShoppingCartItemList(userId: String) {
        _shoppingCartState.value = ShoppingCartState(isLoading = true)
        viewModelScope.launch {
            when(val result = shoppingCartUseCases.getProductsInShoppingCartUseCase(userId)) {
                is Result.Error -> handleGetShoppingCartItemsError(result.error)
                is Result.Success -> {
                    _shoppingCartState.value = ShoppingCartState(
                        shoppingCartItemList = result.data.toMutableList()
                    )
                    TODO("CALCULATE TOTAL PRICE")
                }
            }
        }
    }

    private fun handleGetShoppingCartItemsError(error: DataError.Local) {
        when(error) {
            DataError.Local.QUERY_FAILED -> {}
            DataError.Local.INSERTION_FAILED -> {}
            DataError.Local.UPDATE_FAILED -> {}
            DataError.Local.DELETION_FAILED -> {}
            DataError.Local.NOT_FOUND -> {
                _shoppingCartState.value = ShoppingCartState(
                    dataError = "Alışveriş sepetindeki ürünlerin bilgisi alınamadı."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = ShoppingCartState(
                    dataError = "Bilinmeyen bir hata meydana geldi."
                )
            }
        }
    }

    fun removeItemFromShoppingCart(userId: String, productId: String, itemIndex: Int) {
        viewModelScope.launch {
            when(val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
                is Result.Error -> handleRemoveItemFromShoppingCartError(result.error)
                is Result.Success -> removeItemFromShoppingCartItemList(itemIndex)
            }
        }
    }

    private fun handleRemoveItemFromShoppingCartError(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _shoppingCartState.value = _shoppingCartState.value!!.copy(
                    actionError = "Ürün, alışveriş sepetinden silinemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = _shoppingCartState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }
    }

    private fun removeItemFromShoppingCartItemList(itemIndex: Int) {
        val currentShoppingCartItems = _shoppingCartState.value!!.shoppingCartItemList
        if (itemIndex >= 0 && itemIndex < currentShoppingCartItems.size) {
            currentShoppingCartItems.removeAt(itemIndex)
        }
        _shoppingCartState.value = shoppingCartState.value!!.copy(
            shoppingCartItemList = currentShoppingCartItems
        )
        TODO("CALCULATE TOTAL PRICE")
    }

    fun increaseItemQuantityInShoppingCart(userId: String, productId: String, currentQuantity: Int, itemIndex: Int) {
        viewModelScope.launch {
            val increasedItemQuantity = currentQuantity + 1
            val result = shoppingCartUseCases.updateShoppingCartItemEntityUseCase(userId, productId, increasedItemQuantity)
            when(result) {
                is Result.Error -> handleIncreaseItemQuantityError(result.error)
                is Result.Success -> increaseItemQuantityInShoppingCartItemList(increasedItemQuantity, itemIndex)
            }
        }
    }

    private fun handleIncreaseItemQuantityError(error: DataError.Local) {
        when(error) {
            DataError.Local.UPDATE_FAILED -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "Ürün adedi arttırılamadı."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }
    }

    private fun increaseItemQuantityInShoppingCartItemList(increasedItemQuantity: Int, itemIndex: Int) {
        val currentShoppingCartItemList = _shoppingCartState.value!!.shoppingCartItemList
        currentShoppingCartItemList[itemIndex].quantity = increasedItemQuantity
        _shoppingCartState.value = _shoppingCartState.value!!.copy(
            shoppingCartItemList = currentShoppingCartItemList
        )
        TODO("CALCULATE TOTAL PRICE")
    }

    fun decreaseItemQuantityInShoppingCart(userId: String, productId: String, currentQuantity: Int, itemIndex: Int) {
        val decreasedItemQuantity = currentQuantity - 1
        if (decreasedItemQuantity == 0) {
            removeItemFromShoppingCart(userId, productId, itemIndex)
        } else {
            viewModelScope.launch {
                val result = shoppingCartUseCases.updateShoppingCartItemEntityUseCase(userId, productId, decreasedItemQuantity)
                when(result) {
                    is Result.Error -> handleIncreaseItemQuantityError(result.error)
                    is Result.Success -> TODO()
                }
            }
        }
    }

    private fun handleDecreaseItemQuantityError(error: DataError.Local) {
        when(error) {
            DataError.Local.UPDATE_FAILED -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "Ürün adedi azaltılamadı."
                )
            }
            DataError.Local.UNKNOWN -> {
                _shoppingCartState.value = shoppingCartState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }
    }

    private fun decreaseItemQuantityInShoppingCartList(decreasedItemQuantity: Int, itemIndex: Int) {
        val currentShoppingCartItemList = _shoppingCartState.value!!.shoppingCartItemList
        currentShoppingCartItemList[itemIndex].quantity = decreasedItemQuantity
        _shoppingCartState.value = _shoppingCartState.value!!.copy(
            shoppingCartItemList = currentShoppingCartItemList
        )
        TODO("CALCULATE TOTAL PRICE")
    }

    private fun calculateTotalPriceInShoppingCart() {

    }

    /*
    private var _stateShoppingCartItems = MutableLiveData<Resource<MutableList<ShoppingCartItem>>>()
    val stateShoppingCartItems: LiveData<Resource<MutableList<ShoppingCartItem>>>
        get() = _stateShoppingCartItems

    private var _stateTotal = MutableLiveData<String>()
    val stateTotal: LiveData<String>
        get() = _stateTotal

    private var _stateShoppingCartItemSize = MutableLiveData<Int>()
    val stateShoppingCartItemSize: LiveData<Int>
        get() = _stateShoppingCartItemSize
     */

    /*
    fun getShoppingCartList(userId: String) {
        val tempShoppingCartList = mutableListOf<ShoppingCartItem>()
        _stateShoppingCartItems.value = Resource.Loading(null)

        viewModelScope.launch {
            val responseFromShoppingCartItemsEntity = homeRepository.getShoppingCartItems(userId)
            if (responseFromShoppingCartItemsEntity is Resource.Success) {
                val shoppingCartItemsEntityList = responseFromShoppingCartItemsEntity.data

                if (!(shoppingCartItemsEntityList.isNullOrEmpty())) {
                    val responseFromProduct = homeRepository.getProducts()

                    if (responseFromProduct is Resource.Success) {
                        val productEntityList = responseFromProduct.data?: listOf()

                        for (productEntity in productEntityList) {

                            for (shoppingCartEntity in shoppingCartItemsEntityList) {
                                if (productEntity.productId.toString() == shoppingCartEntity.productId) {
                                    tempShoppingCartList.add(
                                        ShoppingCartItem(
                                        productId = productEntity.productId.toString(),
                                        category = productEntity.productCategory!!,
                                        photo = productEntity.productPhoto!!,
                                        brand = productEntity.productBrand!!,
                                        detail = productEntity.productDetail!!,
                                        priceWhole = productEntity.productPriceWhole!!,
                                        priceCent = productEntity.productPriceCent!!,
                                        quantity = shoppingCartEntity.quantity!!.toInt()
                                    )
                                    )
                                }
                            }
                        }

                        _stateShoppingCartItems.value = Resource.Success(tempShoppingCartList)
                        calculateTotalPriceAndShoppingCartQuantity()
                    } else {
                    }

                } else {
                    _stateShoppingCartItems.value = Resource.Success(mutableListOf())
                    calculateTotalPriceAndShoppingCartQuantity()
                }

            } else {
            }
        }
    }
 */

    /*
    fun increaseShoppingCartItem(userId: String, productId: String) {
        val tempMutableList = _stateShoppingCartItems.value!!.data
        viewModelScope.launch {
            tempMutableList?.let {shoppingCartItems ->
                for (item in shoppingCartItems) {
                    if (item.productId == productId) {
                        val increasedQuantity = item.quantity + 1

                        val response = homeRepository.updateShoppingCartItem(
                            userId = userId,
                            productId = productId,
                            quantity = increasedQuantity.toString()
                        )

                        if (response is Resource.Success) {
                            item.quantity = increasedQuantity
                            _stateShoppingCartItems.value = Resource.Success(tempMutableList)
                            calculateTotalPriceAndShoppingCartQuantity()
                        } else {
                        }
                    }
                }
            }
        }
    }
     */

    /*
    fun decreaseShoppingCartItem(userId: String, productId: String) {
        val tempMutableList = _stateShoppingCartItems.value!!.data
        viewModelScope.launch {
            tempMutableList?.let {shoppingCartItems ->
                for (item in shoppingCartItems) {
                    if (item.productId == productId) {
                        val decreasedQuantity = item.quantity - 1

                        if (decreasedQuantity == 0) {
                            deleteShoppingCartItem(userId, productId)
                        } else {
                            val response = homeRepository.updateShoppingCartItem(
                                userId = userId,
                                productId = productId,
                                quantity = decreasedQuantity.toString()
                            )
                            if (response is Resource.Success) {
                                item.quantity = decreasedQuantity
                                _stateShoppingCartItems.value = Resource.Success(tempMutableList)
                                calculateTotalPriceAndShoppingCartQuantity()
                            } else {
                            }
                        }
                    }
                }
            }
        }
    }
     */

    /*
    fun deleteShoppingCartItem(userId: String, productId: String) {
        var tempMutableList = _stateShoppingCartItems.value!!.data
        viewModelScope.launch {
            tempMutableList?.let {shoppingCartItems ->
                for (item in shoppingCartItems) {
                    if (item.productId == productId) {
                        val response = homeRepository.deleteShoppingCartItem(userId = userId, productId = productId)
                        if (response is Resource.Success) {
                            tempMutableList = tempMutableList?.filterIndexed { _, shoppingCartItem ->
                                shoppingCartItem.productId != productId
                            }?.toMutableList()
                            _stateShoppingCartItems.value = Resource.Success(tempMutableList?: mutableListOf())
                            calculateTotalPriceAndShoppingCartQuantity()
                        } else {
                        }
                    }
                }
            }
        }
    }
     */

    /*
    private fun calculateTotalPriceAndShoppingCartQuantity() {
        val tempMutableList = _stateShoppingCartItems.value!!.data

        tempMutableList?.let {shoppingCartMutableList ->
            var totalWholePart = 0
            var totalDecimalPart = 0

            for (item in shoppingCartMutableList) {
                val itemQuantity = item.quantity
                val totalArray = TotalCost.calculateTotalCost(item.priceWhole, item.priceCent, itemQuantity)

                totalWholePart += totalArray[0]
                totalDecimalPart += totalArray[1]
            }

            val totalArray = TotalCost.calculateTotalCost(totalWholePart, totalDecimalPart)
            _stateTotal.value = "${totalArray[0]},${totalArray[1].toCent()} TL"
        }

        _stateShoppingCartItemSize.value = tempMutableList?.size
    }
     */

    /*
    fun saveShoppinCartListToSingleton() {
        ShoppingCartList.shoppingCartList = _stateShoppingCartItems.value!!.data!!.toList()
        ShoppingCartList.totalPrice = _stateTotal.value!!
    }
     */
}