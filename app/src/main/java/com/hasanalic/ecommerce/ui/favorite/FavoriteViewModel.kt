package com.hasanalic.ecommerce.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.domain.model.Product
import com.hasanalic.ecommerce.data.dto.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.domain.repository.HomeRepository
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private var _stateFavorites = MutableLiveData<Resource<MutableList<Product>>>()
    val stateFavorites : LiveData<Resource<MutableList<Product>>>
        get() = _stateFavorites

    private var _stateShoppingCartItemSize = MutableLiveData<Int>()
    val stateShoppingCartItemSize: LiveData<Int>
        get() = _stateShoppingCartItemSize

    fun getShoppingCartCount(userId: String) {
        viewModelScope.launch {
            val response = homeRepository.getShoppingCartCount(userId)
            if (response is Resource.Success) {
                _stateShoppingCartItemSize.value = response.data!!
            } else {
            }
        }
    }

    fun getFavorites(userId: String) {
        val tempProductList = mutableListOf<Product>()
        _stateFavorites.value = Resource.Loading(null)
        viewModelScope.launch {
            val responseFromFavorite = homeRepository.getFavorites(userId)
            if (responseFromFavorite is Resource.Success) {
                val favoriteList = responseFromFavorite.data

                if (!(favoriteList.isNullOrEmpty())) {
                    val responseFromProduct = homeRepository.getFavoriteProducts(favoriteList.map { it.productId.toString() })

                    if (responseFromProduct is Resource.Success) {
                        val productList = responseFromProduct.data?: listOf()

                        val responseFromShoppingCart = homeRepository.getShoppingCartItems(userId)
                        if (responseFromShoppingCart is Resource.Success) {
                            val shoppingCartList = responseFromShoppingCart.data?: listOf()
                            val addedToShoppingCartProductIdList = shoppingCartList.map { it.productId }

                            for (productEntity in productList) {
                                var addedToCart = false
                                for (addedToShoppingCartProduct in addedToShoppingCartProductIdList) {
                                    if (productEntity.productId.toString() == addedToShoppingCartProduct) {
                                        // bu product'ın shopping cart özelliği true olmalı
                                        addedToCart = true
                                        break
                                    }
                                }
                                tempProductList.add(Product(
                                    productId = productEntity.productId.toString(),
                                    productCategory = productEntity.productCategory!!,
                                    productPhoto = productEntity.productPhoto!!,
                                    productBrand = productEntity.productBrand!!,
                                    productDetail = productEntity.productDetail!!,
                                    productPriceWhole = productEntity.productPriceWhole!!,
                                    productPriceCent = productEntity.productPriceCent!!,
                                    productRate = productEntity.productRate!!,
                                    productReviewCount = productEntity.productReviewCount!!,
                                    productBarcode = productEntity.productBarcode!!,
                                    addedToFavorites = true,
                                    addedToShoppingCart = addedToCart
                                ))
                                _stateFavorites.value = Resource.Success(tempProductList)
                            }
                        } else {
                        }
                    } else {
                    }
                } else {
                    _stateFavorites.value = Resource.Success(mutableListOf())
                }
            } else {
            }
        }
    }

    fun unFavorite(userId: String, productId: String) {
        var tempMutableList = _stateFavorites.value!!.data

        viewModelScope.launch {
            val response = homeRepository.deleteFavorite(userId, productId)
            if (response is Resource.Success) {
                tempMutableList = tempMutableList?.filterIndexed { _, product ->
                    product.productId != productId
                }?.toMutableList()
                _stateFavorites.value = Resource.Success(tempMutableList?: mutableListOf())
            } else {
            }
        }
    }

    fun changeAddToShoppingCart(userId: String, productId: String) {
        var tempMutableList = _stateFavorites.value!!.data

        viewModelScope.launch {
            tempMutableList?.let { products ->
                for (product in products) {
                    if (product.productId == productId) {
                        var isAddedToShoppingCart = !(product.addedToShoppingCart)

                        if (isAddedToShoppingCart) {
                            val response = homeRepository.insertShoppingCartItems(
                                ShoppingCartItemsEntity(
                                userId,productId,"1"
                            )
                            )
                            if (response is Resource.Success) {
                                product.addedToShoppingCart = isAddedToShoppingCart
                            } else {
                            }
                        } else {
                            val response = homeRepository.deleteShoppingCartItem(userId, productId)
                            if (response is Resource.Success) {
                                product.addedToShoppingCart = isAddedToShoppingCart
                            } else {
                            }
                        }
                    }
                }
            }

            getShoppingCartCount(userId)
            _stateFavorites.value = Resource.Success(tempMutableList?: mutableListOf())
        }
    }
}