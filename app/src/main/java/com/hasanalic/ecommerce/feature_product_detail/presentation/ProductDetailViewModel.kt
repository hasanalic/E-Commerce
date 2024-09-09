package com.hasanalic.ecommerce.feature_product_detail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_product_detail.domain.use_cases.ProductDetailUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor (
    private val productDetailUseCases: ProductDetailUseCases,
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val favoriteUseCases: FavoriteUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _productDetailState = MutableLiveData(ProductDetailState())
    val productDetailState: LiveData<ProductDetailState> = _productDetailState

    init {
        getUserIdIfExists()
    }

    private fun getUserIdIfExists() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        userId?.let {
            _productDetailState.value = ProductDetailState(userId = it)
        }
    }

    fun getProductDetailAndReviews(productId: String) {
        _productDetailState.value = _productDetailState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val userId = _productDetailState.value!!.userId
            val result = productDetailUseCases.getProductDetailByUserIdAndProductIdUseCase(userId, productId)
            when(result) {
                is Result.Error -> handleGetProductDetailError(result.error)
                is Result.Success -> {
                    _productDetailState.value = _productDetailState.value!!.copy(
                        isLoading = false,
                        productDetail = result.data
                    )

                    getProductReviews(productId)
                }
            }
        }
    }

    private fun handleGetProductDetailError(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _productDetailState.value = ProductDetailState(
                    isLoading = false,
                    dataError = "Product information could not be retrieved."
                )
            }
            DataError.Local.UNKNOWN -> {
                _productDetailState.value = ProductDetailState(
                    isLoading = false,
                    dataError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun getProductReviews(productId: String) {
        viewModelScope.launch {
            when(val result = productDetailUseCases.getReviewsByProductIdUseCase(productId)) {
                is Result.Error -> handleGetProductReviews(result.error)
                is Result.Success -> {
                    _productDetailState.value = _productDetailState.value!!.copy(
                        isLoading = false,
                        reviewList = result.data
                    )
                }
            }
        }
    }

    private fun handleGetProductReviews(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    dataError = "No comments were received regarding the product."
                )
            }
            DataError.Local.UNKNOWN -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    dataError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun addProductToCart(productId: String) {
        viewModelScope.launch {
            val userId = _productDetailState.value!!.userId
            val shoppingCartItemsEntity = ShoppingCartItemsEntity(userId, productId, 1)
            val result = shoppingCartUseCases.insertShoppingCartItemEntityUseCase(shoppingCartItemsEntity)
            when(result) {
                is Result.Error -> handleAddProductToCartError(result.error)
                is Result.Success -> {
                    val currentProductDetail = _productDetailState.value!!.productDetail
                    currentProductDetail?.let { it.addedToShoppingCart = true }

                    _productDetailState.value = _productDetailState.value!!.copy(
                        productDetail = currentProductDetail
                    )
                }
            }
        }
    }

    private fun handleAddProductToCartError(error: DataError.Local) {
        when(error) {
            DataError.Local.INSERTION_FAILED -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "The product could not be added to the shopping cart."
                )
            }
            DataError.Local.UNKNOWN -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun removeProductFromCart(productId: String) {
        viewModelScope.launch {
            val userId = _productDetailState.value!!.userId
            val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)
            when(result) {
                is Result.Error -> handleRemoveProductFromCart(result.error)
                is Result.Success -> {
                    val currentProductDetail = _productDetailState.value!!.productDetail
                    currentProductDetail?.let { it.addedToShoppingCart = false }

                    _productDetailState.value = _productDetailState.value!!.copy(
                        productDetail = currentProductDetail
                    )
                }
            }
        }
    }

    private fun handleRemoveProductFromCart(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "The item could not be removed from the shopping cart."
                )
            }
            DataError.Local.UNKNOWN -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun addProductToFavoritesIfUserLoggedIn(productId: String) {
        val userId = _productDetailState.value!!.userId

        if (userId == ANOMIM_USER_ID) {
            _productDetailState.value = _productDetailState.value!!.copy(
                shouldUserMoveToAuthActivity = true
            )
            return
        }

        viewModelScope.launch {
            val favoritesEntity = FavoritesEntity(userId, productId)
            when(val result = favoriteUseCases.insertFavoriteAndGetIdUseCase(favoritesEntity)) {
                is Result.Error -> handleAddProductToFavorites(result.error)
                is Result.Success -> {
                    val currentProductDetail = _productDetailState.value!!.productDetail
                    currentProductDetail?.let { it.addedToFavorites = true }

                    _productDetailState.value = _productDetailState.value!!.copy(
                        productDetail = currentProductDetail
                    )
                }
            }
        }
    }

    private fun handleAddProductToFavorites(error: DataError.Local) {
        when(error) {
            DataError.Local.INSERTION_FAILED -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "The product could not be added to favorites."
                )
            }
            DataError.Local.UNKNOWN -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }

    fun removeProductFromFavoritesIfUserLoggedIn(productId: String) {
        val userId = _productDetailState.value!!.userId

        if (userId == ANOMIM_USER_ID) {
            _productDetailState.value = _productDetailState.value!!.copy(
                shouldUserMoveToAuthActivity = true
            )
            return
        }

        viewModelScope.launch {
            when(val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromFavorites(result.error)
                is Result.Success -> {
                    val currentProductDetail = _productDetailState.value!!.productDetail
                    currentProductDetail?.let { it.addedToFavorites = false }

                    _productDetailState.value = _productDetailState.value!!.copy(
                        productDetail = currentProductDetail
                    )
                }
            }
        }
    }

    private fun handleRemoveProductFromFavorites(error: DataError.Local) {
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "The product could not be removed from favorites."
                )
            }
            DataError.Local.UNKNOWN -> {
                _productDetailState.value = _productDetailState.value!!.copy(
                    isLoading = false,
                    actionError = "An unknown error occurred."
                )
            }
            else -> {}
        }
    }
}