package com.hasanalic.ecommerce.feature_home.presentation.favorite_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCases: FavoriteUseCases,
    private val shoppingCartUseCases: ShoppingCartUseCases
) : ViewModel() {

    private val _favoriteState = MutableLiveData(FavoriteState())
    var favoriteState: LiveData<FavoriteState> = _favoriteState

    fun getUserFavoriteProducts(userId: String) {
        _favoriteState.value = FavoriteState(isLoading = true)
        viewModelScope.launch {
            when (val result = favoriteUseCases.getFavoriteProductsUseCase(userId)) {
                is Result.Error -> handleGetFavoriteProductsError(result.error)
                is Result.Success -> _favoriteState.value = FavoriteState(
                    favoriteProductList = result.data.toMutableList()
                )
            }
        }
    }

    private fun handleGetFavoriteProductsError(error: DataError.Local) {
        when (error) {
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
            else -> {}
        }
    }

    fun removeProductFromFavorites(userId: String, productId: String, itemIndex: Int) {
        viewModelScope.launch {
            when (val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
                is Result.Error -> handleDeleteFavoriteError(result.error)
                is Result.Success -> removeProductFromMutableFavoriteProductList(itemIndex)
            }
        }
    }

    private fun handleDeleteFavoriteError(error: DataError.Local) {
        when (error) {
            DataError.Local.DELETION_FAILED -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "ürün favorilerden kaldırılamadı."
                )
            }
            DataError.Local.UNKNOWN -> {
                _favoriteState.value = _favoriteState.value!!.copy(
                    actionError = "Bilinmeyen bir nedenden dolayı, ürün favorilerden kaldırılamadı."
                )
            }
            else -> {}
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
        viewModelScope.launch {
            val shoppingCartEntity = ShoppingCartItemsEntity(userId, productId, 1)
            when (val result = shoppingCartUseCases.insertShoppingCartItemEntityUseCase(shoppingCartEntity)) {
                is Result.Error -> handleAddProductToCartError(result.error)
                is Result.Success -> setAddedToCart(itemIndex, true)
            }
        }
    }

    private fun handleAddProductToCartError(error: DataError.Local) {
        when (error) {
            DataError.Local.INSERTION_FAILED -> {
                _favoriteState.value = FavoriteState(
                    actionError = "Ürün alışveriş sepetine eklenemedi."
                )
            }

            DataError.Local.UNKNOWN -> {
                _favoriteState.value = FavoriteState(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }

            else -> {}
        }
    }

    fun removeProductFromCart(userId: String, productId: String, itemIndex: Int) {
        viewModelScope.launch {
            when (val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromCartError(result.error)
                is Result.Success -> setAddedToCart(itemIndex, false)
            }
        }
    }

    private fun handleRemoveProductFromCartError(error: DataError.Local) {
        when (error) {
            DataError.Local.DELETION_FAILED -> {
                _favoriteState.value = FavoriteState(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }

            DataError.Local.UNKNOWN -> {
                _favoriteState.value = FavoriteState(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }

            else -> {}
        }
    }

    private fun setAddedToCart(itemIndex: Int, isAddedToCart: Boolean) {
        val currentFavoriteProductList = _favoriteState.value!!.favoriteProductList
        currentFavoriteProductList[itemIndex].addedToShoppingCart = isAddedToCart
        _favoriteState.value = _favoriteState.value!!.copy(
            favoriteProductList = currentFavoriteProductList
        )
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