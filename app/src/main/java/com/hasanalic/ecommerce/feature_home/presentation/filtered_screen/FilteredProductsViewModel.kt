package com.hasanalic.ecommerce.feature_home.presentation.filtered_screen

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
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.HomeUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import com.hasanalic.ecommerce.feature_home.presentation.home_screen.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilteredProductsViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val favoriteUseCases: FavoriteUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _filteredProductsState = MutableLiveData(FilteredProductsState())
    val filteredProducsState: LiveData<FilteredProductsState> = _filteredProductsState


    fun getProductsBySearchKey(searchKey: String) {
        _filteredProductsState.value = _filteredProductsState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            /*
            val userId = _homeState.value!!.userId

            when(val result = homeUseCases.getProductsByUserIdUseCase(userId)) {
                is Result.Error -> handleGetProductsError(result.error)
                is Result.Success -> {
                    _homeState.value = homeState.value!!.copy(
                        isLoading = false,
                        productList = result.data.toMutableList()
                    )
                }
            }

             */
        }
    }

    private fun handleGetProductsBySearchKeyError(error: DataError.Local) {
        /*
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _homeState.value = HomeState(
                    dataError = "Ürünler getirilemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = HomeState(
                    dataError = "Bilinmeyen bir hata nedeniyle ürünler getirilemedi."
                )
            }
            else -> {}
        }

         */
    }

    fun removeProductFromCart(productId: String, itemIndex: Int) {
        /*
        viewModelScope.launch {
            val userId = _homeState.value!!.userId

            when(val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromCartError(result.error)
                is Result.Success -> { setAddedToCart(itemIndex, false) }
            }
        }

         */
    }

    private fun handleRemoveProductFromCartError(error: DataError.Local) {
        /*
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Ürün, alışveriş sepetinden silinemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }

         */
    }

    fun addProductToCart(productId: String, itemIndex: Int) {
        /*
        viewModelScope.launch {
            val userId = _homeState.value!!.userId
            val shoppingCartEntity = ShoppingCartItemsEntity(userId, productId, 1)

            when (val result = shoppingCartUseCases.insertShoppingCartItemEntityUseCase(shoppingCartEntity)) {
                is Result.Error -> handleAddProductToCartError(result.error)
                is Result.Success -> setAddedToCart(itemIndex, true)
            }
        }

         */
    }

    private fun handleAddProductToCartError(error: DataError.Local) {
        /*
        when(error) {
            DataError.Local.INSERTION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Ürün, alışveriş sepetine eklenemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }

         */
    }

    private fun setAddedToCart(itemIndex: Int, isAddedToCart: Boolean) {
        /*
        val currentProductList = _homeState.value!!.productList
        currentProductList[itemIndex].addedToShoppingCart = isAddedToCart
        _homeState.value = _homeState.value!!.copy(
            productList = currentProductList
        )

         */
    }

    fun removeProductFromFavoritesIfUserAuthenticated(productId: String, itemIndex: Int) {
        /*
        val userId = _homeState.value!!.userId

        if (userId == ANOMIM_USER_ID) {
            _homeState.value = _homeState.value!!.copy(
                shouldUserMoveToAuthActivity = true
            )
            return
        }

        viewModelScope.launch {
            when(val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromFavoritesError(result.error)
                is Result.Success -> { setAddedToFavorites(itemIndex, false) }
            }
        }

         */
    }

    private fun handleRemoveProductFromFavoritesError(error: DataError.Local) {
        /*
        when(error) {
            DataError.Local.DELETION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Ürün, favorilerden kaldırılamadı."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }

         */
    }

    fun addProductToFavoritesIfUserAuthenticated(productId: String, itemIndex: Int) {
        /*
        val userId = _homeState.value!!.userId

        if (userId == ANOMIM_USER_ID) {
            _homeState.value = _homeState.value!!.copy(
                shouldUserMoveToAuthActivity = true
            )
            return
        }

        viewModelScope.launch {
            val favoriteEntity = FavoritesEntity(userId, productId)
            when(val result = favoriteUseCases.insertFavoriteAndGetIdUseCase(favoriteEntity)) {
                is Result.Error -> handleAddProductToFavoritesError(result.error)
                is Result.Success -> { setAddedToFavorites(itemIndex, true) }
            }
        }

         */
    }

    private fun handleAddProductToFavoritesError(error: DataError.Local) {
        /*
        when(error) {
            DataError.Local.INSERTION_FAILED -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Ürün, favorilere eklenemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = _homeState.value!!.copy(
                    actionError = "Bilinmeyen bir hata meydana geldi."
                )
            }
            else -> {}
        }

         */
    }

    private fun setAddedToFavorites(itemIndex: Int, isAddedToFavorites: Boolean) {
        /*
        val currentProductList = _homeState.value!!.productList
        currentProductList[itemIndex].addedToFavorites = isAddedToFavorites
        _homeState.value = _homeState.value!!.copy(
            productList = currentProductList
        )

         */
    }
}