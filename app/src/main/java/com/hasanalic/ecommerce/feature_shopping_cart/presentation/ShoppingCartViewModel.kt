package com.hasanalic.ecommerce.feature_shopping_cart.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.feature_checkout.presentation.ShoppingCartList
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

    fun saveShoppinCartListToSingleton() {
        ShoppingCartList.shoppingCartList = _stateShoppingCartItems.value!!.data!!.toList()
        ShoppingCartList.totalPrice = _stateTotal.value!!
    }
}