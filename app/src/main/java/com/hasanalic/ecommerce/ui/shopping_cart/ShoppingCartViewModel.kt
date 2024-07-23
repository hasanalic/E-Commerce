package com.hasanalic.ecommerce.ui.shopping_cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.domain.repository.HomeRepository
import com.hasanalic.ecommerce.ui.checkout.ShoppingCartList
import com.hasanalic.ecommerce.utils.Resource
import com.hasanalic.ecommerce.utils.TotalCost
import com.hasanalic.ecommerce.utils.toCent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private var _stateShoppingCartItems = MutableLiveData<Resource<MutableList<ShoppingCartItem>>>()
    val stateShoppingCartItems: LiveData<Resource<MutableList<ShoppingCartItem>>>
        get() = _stateShoppingCartItems

    private var _stateTotal = MutableLiveData<String>()
    val stateTotal: LiveData<String>
        get() = _stateTotal

    private var _stateShoppingCartItemSize = MutableLiveData<Int>()
    val stateShoppingCartItemSize: LiveData<Int>
        get() = _stateShoppingCartItemSize

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
                                    tempShoppingCartList.add(ShoppingCartItem(
                                        shoppingCartItemId = productEntity.productId.toString(),
                                        shoppingCartItemCategory = productEntity.productCategory!!,
                                        shoppingCartItemPhoto = productEntity.productPhoto!!,
                                        shoppingCartItemBrand = productEntity.productBrand!!,
                                        shoppingCartItemDetail = productEntity.productDetail!!,
                                        shoppingCartItemPriceWhole = productEntity.productPriceWhole!!,
                                        shoppingCartItemPriceCent = productEntity.productPriceCent!!,
                                        shoppingCartItemQuantity = shoppingCartEntity.quantity!!.toInt()
                                    ))
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

    fun increaseShoppingCartItem(userId: String, productId: String) {
        val tempMutableList = _stateShoppingCartItems.value!!.data
        viewModelScope.launch {
            tempMutableList?.let {shoppingCartItems ->
                for (item in shoppingCartItems) {
                    if (item.shoppingCartItemId == productId) {
                        val increasedQuantity = item.shoppingCartItemQuantity + 1

                        val response = homeRepository.updateShoppingCartItem(
                            userId = userId,
                            productId = productId,
                            quantity = increasedQuantity.toString()
                        )

                        if (response is Resource.Success) {
                            item.shoppingCartItemQuantity = increasedQuantity
                            _stateShoppingCartItems.value = Resource.Success(tempMutableList)
                            calculateTotalPriceAndShoppingCartQuantity()
                        } else {
                        }
                    }
                }
            }
        }
    }

    fun decreaseShoppingCartItem(userId: String, productId: String) {
        val tempMutableList = _stateShoppingCartItems.value!!.data
        viewModelScope.launch {
            tempMutableList?.let {shoppingCartItems ->
                for (item in shoppingCartItems) {
                    if (item.shoppingCartItemId == productId) {
                        val decreasedQuantity = item.shoppingCartItemQuantity - 1

                        if (decreasedQuantity == 0) {
                            deleteShoppingCartItem(userId, productId)
                        } else {
                            val response = homeRepository.updateShoppingCartItem(
                                userId = userId,
                                productId = productId,
                                quantity = decreasedQuantity.toString()
                            )
                            if (response is Resource.Success) {
                                item.shoppingCartItemQuantity = decreasedQuantity
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

    fun deleteShoppingCartItem(userId: String, productId: String) {
        var tempMutableList = _stateShoppingCartItems.value!!.data
        viewModelScope.launch {
            tempMutableList?.let {shoppingCartItems ->
                for (item in shoppingCartItems) {
                    if (item.shoppingCartItemId == productId) {
                        val response = homeRepository.deleteShoppingCartItem(userId = userId, productId = productId)
                        if (response is Resource.Success) {
                            tempMutableList = tempMutableList?.filterIndexed { _, shoppingCartItem ->
                                shoppingCartItem.shoppingCartItemId != productId
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

    private fun calculateTotalPriceAndShoppingCartQuantity() {
        val tempMutableList = _stateShoppingCartItems.value!!.data

        tempMutableList?.let {shoppingCartMutableList ->
            var totalWholePart = 0
            var totalDecimalPart = 0

            for (item in shoppingCartMutableList) {
                val itemQuantity = item.shoppingCartItemQuantity
                val totalArray = TotalCost.calculateTotalCost(item.shoppingCartItemPriceWhole, item.shoppingCartItemPriceCent, itemQuantity)

                totalWholePart += totalArray[0]
                totalDecimalPart += totalArray[1]
            }

            val totalArray = TotalCost.calculateTotalCost(totalWholePart, totalDecimalPart)
            _stateTotal.value = "${totalArray[0]},${totalArray[1].toCent()} TL"
        }

        _stateShoppingCartItemSize.value = tempMutableList?.size
    }

    fun saveShoppinCartListToSingleton() {
        ShoppingCartList.shoppingCartList = _stateShoppingCartItems.value!!.data!!.toList()
        ShoppingCartList.totalPrice = _stateTotal.value!!
    }
}