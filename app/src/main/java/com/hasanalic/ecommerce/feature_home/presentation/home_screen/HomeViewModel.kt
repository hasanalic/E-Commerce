package com.hasanalic.ecommerce.feature_home.presentation.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.use_case.favorite_use_cases.FavoriteUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases.HomeUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.shopping_cart_use_cases.ShoppingCartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val shoppingCartUseCases: ShoppingCartUseCases,
    private val favoriteUseCases: FavoriteUseCases
) : ViewModel() {

    private var _homeState = MutableLiveData(HomeState())
    val homeState: LiveData<HomeState> = _homeState

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            when(val result = homeUseCases.getCategoriesUseCase()) {
                is Result.Error -> handleGetCategoriesError(result.error)
                is Result.Success -> {
                    _homeState.value = _homeState.value!!.copy(
                        isLoading = false,
                        categoryList = result.data.toMutableList()
                    )
                }
            }
        }
    }

    private fun handleGetCategoriesError(error: DataError.Local) {
        when(error) {
            DataError.Local.NOT_FOUND -> {
                _homeState.value = HomeState(
                    dataError = "Kategoriler getirilemedi."
                )
            }
            DataError.Local.UNKNOWN -> {
                _homeState.value = HomeState(
                    dataError = "Bilinmeyen bir hata nedeniyle kategoriler getirilemedi."
                )
            }
            else -> {}
        }
    }

    fun getProducts(userId: String) {
        _homeState.value = _homeState.value!!.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when(val result = homeUseCases.getProductsByUserIdUseCase(userId)) {
                is Result.Error -> handleGetProductsError(result.error)
                is Result.Success -> {
                    _homeState.value = homeState.value!!.copy(
                        isLoading = false,
                        productList = result.data.toMutableList()
                    )
                }
            }
        }
    }

    private fun handleGetProductsError(error: DataError.Local) {
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
    }

    fun removeProductFromCart(userId: String, productId: String, itemIndex: Int) {
        viewModelScope.launch {
            when(val result = shoppingCartUseCases.deleteShoppingCartItemEntityUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromCartError(result.error)
                is Result.Success -> { setAddedToCart(itemIndex, false) }
            }
        }
    }

    private fun handleRemoveProductFromCartError(error: DataError.Local) {
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
    }

    private fun setAddedToCart(itemIndex: Int, isAddedToCart: Boolean) {
        val currentProductList = _homeState.value!!.productList
        currentProductList[itemIndex].addedToShoppingCart = isAddedToCart
        _homeState.value = _homeState.value!!.copy(
            productList = currentProductList
        )
    }

    fun removeProductFromFavorites(userId: String, productId: String, itemIndex: Int) {
        viewModelScope.launch {
            when(val result = favoriteUseCases.deleteFavoriteUseCase(userId, productId)) {
                is Result.Error -> handleRemoveProductFromFavoritesError(result.error)
                is Result.Success -> { setAddedToFavorites(itemIndex, false) }
            }
        }
    }

    private fun handleRemoveProductFromFavoritesError(error: DataError.Local) {
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
    }

    fun addProductToFavorites(userId: String, productId: String, itemIndex: Int) {
        viewModelScope.launch {
            val favoriteEntity = FavoritesEntity(userId, productId)
            when(val result = favoriteUseCases.insertFavoriteAndGetIdUseCase(favoriteEntity)) {
                is Result.Error -> handleAddProductToFavoritesError(result.error)
                is Result.Success -> { setAddedToFavorites(itemIndex, true) }
            }
        }
    }

    private fun handleAddProductToFavoritesError(error: DataError.Local) {
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
    }

    private fun setAddedToFavorites(itemIndex: Int, isAddedToFavorites: Boolean) {
        val currentProductList = _homeState.value!!.productList
        currentProductList[itemIndex].addedToFavorites = isAddedToFavorites
        _homeState.value = _homeState.value!!.copy(
            productList = currentProductList
        )
    }


    /*
    private var _stateProductId = MutableLiveData<Resource<Int>>()
    val stateProductId: LiveData<Resource<Int>>
        get() = _stateProductId

    /// FILTERED PRODUCTS FRAGMENT ///

    fun getProductIdByBarcode(barcode: String) {
        _stateProductId.value = Resource.Loading()
        viewModelScope.launch {
            _stateProductId.value = homeRepository.getProductIdByBarcode(barcode)
        }
    }

    fun resetProductIdStatus() {
        _stateProductId = MutableLiveData<Resource<Int>>()
    }

    fun resetFilteredProductsStatus() {
        _stateFilteredProducts = MutableLiveData<Resource<MutableList<Product>>>()
    }
     */
}