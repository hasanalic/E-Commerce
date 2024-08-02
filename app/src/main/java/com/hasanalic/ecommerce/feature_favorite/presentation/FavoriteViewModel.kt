package com.hasanalic.ecommerce.feature_favorite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_favorite.domain.use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCases: FavoriteUseCases
) : ViewModel() {

    private val _favoriteState = MutableLiveData(FavoriteState())
    var favoriteState: LiveData<FavoriteState> = _favoriteState

    fun getUserFavoriteProducts(userId: String) {
        _favoriteState.value = FavoriteState(isLoading = true)
        viewModelScope.launch {
            when(val result = favoriteUseCases.getFavoriteProductsUseCase(userId)) {
                is Result.Error -> handleGetFavoriteProductsError(result.error)
                is Result.Success -> _favoriteState.value = FavoriteState(
                    favoriteProductList = result.data.toMutableList()
                )
            }
        }
    }

    private fun handleGetFavoriteProductsError(error: DataError.Local) {
        when(error) {
            DataError.Local.QUERY_FAILED -> {}
            DataError.Local.INSERTION_FAILD -> {}
            DataError.Local.UPDATE_FAILED -> {}
            DataError.Local.DELETION_FAILED -> {}
            DataError.Local.NOT_FOUND -> {
                _favoriteState.value = FavoriteState(
                    dataError = "Favori ürün yok."
                )
            }
            DataError.Local.UNKNOWN -> {
                _favoriteState.value = FavoriteState(
                    dataError = "Bilinmeyen bir hata meydana geldi."
                )
            }
        }
    }

    fun removeProductFromFavorites(userId: String, productId: String, itemIndex: Int) {
        viewModelScope.launch {
            when(val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
                is Result.Error -> handleDeleteFavoriteError(result.error)
                is Result.Success -> removeProductFromMutableFavoriteProductList(itemIndex)
            }
        }
    }

    private fun handleDeleteFavoriteError(error: DataError.Local) {
        when(error) {
            DataError.Local.QUERY_FAILED -> {}
            DataError.Local.INSERTION_FAILD -> {}
            DataError.Local.UPDATE_FAILED -> {}
            DataError.Local.DELETION_FAILED -> { _favoriteState.value = _favoriteState.value!!.copy(
                actionError = "ürün favorilerden kaldırılamadı."
            ) }
            DataError.Local.NOT_FOUND -> {}
            DataError.Local.UNKNOWN -> { _favoriteState.value = _favoriteState.value!!.copy(
                actionError = "Bilinmeyen bir nedenden dolayı, ürün favorilerden kaldırılamadı."
            ) }
        }
    }

    private fun removeProductFromMutableFavoriteProductList(itemIndex: Int) {
        val currentFavorites = _favoriteState.value!!.favoriteProductList

        if (itemIndex >= 0 && itemIndex < currentFavorites.size) {
            currentFavorites.removeAt(itemIndex)
        }
        _favoriteState.value = _favoriteState.value!!.copy(
            favoriteProductList = currentFavorites
        )
    }

    fun addProductToCart(userId: String, productId: String, itemIndex: Int) {

    }

    fun removeProductFromCart(userId: String, productId: String, itemIndex: Int) {

    }

    /*
    fun getUserFavoriteProducts(userId: String) {
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
                                tempProductList.add(
                                    Product(
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
                                )
                                )
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
     */

    /*
    fun removeProductFromFavorites(userId: String, productId: String, itemIndex: Int) {
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
     */

    /*
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
     */

    /*
    fun getShoppingCartCount(userId: String) {
        /*
        TODO("Veriyi(ShoppingCart count) almadan viewModel'a sadece 'eksilt'-'arttır' bilgisi ileterek yap")
         */
    }
     */
}