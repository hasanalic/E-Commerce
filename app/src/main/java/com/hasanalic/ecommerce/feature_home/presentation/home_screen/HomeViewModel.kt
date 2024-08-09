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
    private var _stateProducts = MutableLiveData<Resource<MutableList<Product>>>()
    val stateProduct: LiveData<Resource<MutableList<Product>>>
        get() = _stateProducts

    private var _stateCategories = MutableLiveData<List<Chip>>()
    val stateCategories: LiveData<List<Chip>>
        get() = _stateCategories

    private var _stateFilteredProducts = MutableLiveData<Resource<MutableList<Product>>>()
    val stateFilteredProducts: LiveData<Resource<MutableList<Product>>>
        get() = _stateFilteredProducts

    private var _stateCompareCounter = MutableLiveData<Int>()
    val stateCompareCounter: LiveData<Int>
        get() = _stateCompareCounter

    private var _stateShoppingCartItemSize = MutableLiveData<Int>()
    val stateShoppingCartItemSize: LiveData<Int>
        get() = _stateShoppingCartItemSize

    private var _stateProductId = MutableLiveData<Resource<Int>>()
    val stateProductId: LiveData<Resource<Int>>
        get() = _stateProductId

    private var _stateComparedProductIdList = MutableLiveData<MutableList<String>>()
    val stateComparedProductIdList: LiveData<MutableList<String>>
        get() = _stateComparedProductIdList

    private var _stateComparedProductList = MutableLiveData<MutableList<ComparedProduct>>()
    val stateComparedProductList: LiveData<MutableList<ComparedProduct>>
        get() = _stateComparedProductList

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            val response = homeRepository.getCategories()
            if (response is Resource.Success) {
                _stateCategories.value = response.data!!
            } else {
                _stateCategories.value = emptyList()
            }
        }
    }

    fun getShoppingCartCount(userId: String) {
        viewModelScope.launch {
            val response = homeRepository.getShoppingCartCount(userId)
            if (response is Resource.Success) {
                _stateShoppingCartItemSize.value = response.data!!
            } else {
            }
        }
    }

    fun getProducts(userId: String) {
        _stateProducts.value = Resource.Loading(mutableListOf())
        val tempMutableProductList = mutableListOf<Product>()

        if (_stateComparedProductIdList.value == null) {
            _stateComparedProductIdList.value = mutableListOf()
        }

        viewModelScope.launch {
            val responseFromGetProducts = homeRepository.getProducts()

            if (responseFromGetProducts is Resource.Success) {
                val responseFromGetShoppingCartItems = homeRepository.getShoppingCartItems(userId)

                if (responseFromGetShoppingCartItems is Resource.Success) {
                    val responseFromGetFavorites = homeRepository.getFavorites(userId)

                    if (responseFromGetFavorites is Resource.Success) {
                        val productEntityList = responseFromGetProducts.data
                        val shoppingCartItemsEntityList = responseFromGetShoppingCartItems.data
                        val favoritesEntityList = responseFromGetFavorites.data
                        val mutableProductIdList = _stateComparedProductIdList.value!!

                        for (productEntity in productEntityList!!) {
                            var addedToShoppingCart = false
                            var addedToFavorite = false
                            var addedToCompare = false

                            if (shoppingCartItemsEntityList!!.isNotEmpty()) {
                                for (shoppingCartItemEntity in shoppingCartItemsEntityList) {
                                    if (shoppingCartItemEntity.productId == productEntity.productId.toString()) {
                                        addedToShoppingCart = true
                                        break
                                    }
                                }
                            }

                            if (favoritesEntityList!!.isNotEmpty()) {
                                for (favorite in favoritesEntityList) {
                                    if (favorite.productId == productEntity.productId.toString()) {
                                        addedToFavorite = true
                                        break
                                    }
                                }
                            }

                            if (mutableProductIdList.isNotEmpty()) {
                                for (comparedProductId in mutableProductIdList) {
                                    if (comparedProductId == productEntity.productId.toString()) {
                                        addedToCompare = true
                                        break
                                    }
                                }
                            }

                            tempMutableProductList.add(
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
                                    productStore = productEntity.productStore,
                                    productStoreRate = productEntity.productStoreRate,
                                    addedToShoppingCart = addedToShoppingCart,
                                    addedToFavorites = addedToFavorite,
                                    addedToCompare = addedToCompare
                                )
                            )
                        }
                        _stateProducts.value = Resource.Success(tempMutableProductList)
                    } else {
                    }
                } else {
                }
            } else {
                _stateProducts.value = Resource.Error(null,homeRepository.getProducts().message?:"HATA homeRepository.getProducts()")
            }
        }
    }

    fun addShoppingCart(userId: String, productId: String) {
        val tempMutableProductList = _stateProducts.value!!.data
        viewModelScope.launch {
            val response = homeRepository.insertShoppingCartItems(ShoppingCartItemsEntity(userId, productId, 1))
            if (response is Resource.Success) {
                tempMutableProductList?.let {products ->
                    for (product in products) {
                        if (product.productId == productId) {
                            product.addedToShoppingCart = true
                            _stateProducts.value = Resource.Success(tempMutableProductList)
                        }
                    }
                }
            } else {
            }

            getShoppingCartCount(userId)
        }
    }

    fun removeFromShoppingCart(userId: String, productId: String) {
        val tempMutableProductList = _stateProducts.value!!.data
        viewModelScope.launch {
            val response = homeRepository.deleteShoppingCartItem(userId, productId)
            if (response is Resource.Success) {
                tempMutableProductList?.let {products ->
                    for (product in products) {
                        if (product.productId == productId) {
                            product.addedToShoppingCart = false
                            _stateProducts.value = Resource.Success(tempMutableProductList)
                        }
                    }
                }
            } else {
            }

            getShoppingCartCount(userId)
        }
    }

    fun addFavorite(userId: String, productId: String) {
        val tempMutableProductsList = _stateProducts.value!!.data
        viewModelScope.launch {
            val response = homeRepository.insertFavorite(FavoritesEntity(userId = userId, productId = productId))
            if (response is Resource.Success) {
                tempMutableProductsList?.let {products ->
                    for (product in products) {
                        if (product.productId == productId) {
                            product.addedToFavorites = true
                            _stateProducts.value = Resource.Success(tempMutableProductsList)
                        }
                    }
                }
            } else {
            }
        }
    }

    fun removeFromFavorite(userId: String, productId: String) {
        val tempMutableProductsList = _stateProducts.value!!.data
        viewModelScope.launch {
            val response = homeRepository.deleteFavorite(userId, productId)
            if (response is Resource.Success) {
                tempMutableProductsList?.let {products ->
                    for (product in products) {
                        if (product.productId == productId) {
                            product.addedToFavorites = false
                            _stateProducts.value = Resource.Success(tempMutableProductsList)
                        }
                    }
                }
            } else {
            }
        }
    }

    fun clickOnCompare(productId: String) {
        val mutableProductList = _stateProducts.value!!.data
        if (_stateComparedProductIdList.value == null) {
            _stateComparedProductIdList.value = mutableListOf()
        }

        for (product in mutableProductList!!) {
            if (productId == product.productId) {
                val isAddedToCompare = !(product.addedToCompare)
                product.addedToCompare = isAddedToCompare

                var mutableProductIdList = _stateComparedProductIdList.value!!
                if (isAddedToCompare) {
                    mutableProductIdList.add(productId)
                } else {
                    mutableProductIdList = mutableProductIdList.filterIndexed { _, s ->
                        productId != s
                    }.toMutableList()
                }
                _stateComparedProductIdList.value = mutableProductIdList
            }
        }

        calculateTotalCompareCount()

        _stateProducts.value = Resource.Success(mutableProductList)
    }

    /// FILTERED PRODUCTS FRAGMENT ///

    fun getFilteredProductsBySearchQuery(userId: String, searchQuery: String) {
        _stateFilteredProducts.value = Resource.Loading()
        val tempFilteredProductList = mutableListOf<Product>()

        if (_stateComparedProductIdList.value == null) {
            _stateComparedProductIdList.value = mutableListOf()
        }

        viewModelScope.launch {
            val responseFromgetProductsBySearchQuery = homeRepository.getProductsBySearchQuery(searchQuery)

            if (responseFromgetProductsBySearchQuery is Resource.Success) {
                val responseFromGetShoppingCartItems = homeRepository.getShoppingCartItems(userId)

                if (responseFromGetShoppingCartItems is Resource.Success) {
                    val responseFromGetFavorites = homeRepository.getFavorites(userId)

                    if (responseFromGetFavorites is Resource.Success) {
                        val productEntityList = responseFromgetProductsBySearchQuery.data
                        val shoppingCartItemsEntityList = responseFromGetShoppingCartItems.data
                        val favoritesEntityList = responseFromGetFavorites.data
                        val mutableProductIdList = _stateComparedProductIdList.value!!

                        for (productEntity in productEntityList!!) {
                            var addedToShoppingCart = false
                            var addedToFavorite = false
                            var addedToCompare = false

                            if (shoppingCartItemsEntityList!!.isNotEmpty()) {
                                for (shoppingCartItemEntity in shoppingCartItemsEntityList) {
                                    if (shoppingCartItemEntity.productId == productEntity.productId.toString()) {
                                        addedToShoppingCart = true
                                        break
                                    }
                                }
                            }

                            if (favoritesEntityList!!.isNotEmpty()) {
                                for (favorite in favoritesEntityList) {
                                    if (favorite.productId == productEntity.productId.toString()) {
                                        addedToFavorite = true
                                        break
                                    }
                                }
                            }

                            if (mutableProductIdList.isNotEmpty()) {
                                for (comparedProductId in mutableProductIdList) {
                                    if (comparedProductId == productEntity.productId.toString()) {
                                        addedToCompare = true
                                        break
                                    }
                                }
                            }

                            tempFilteredProductList.add(
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
                                    addedToShoppingCart = addedToShoppingCart,
                                    addedToFavorites = addedToFavorite,
                                    addedToCompare = addedToCompare
                                )
                            )
                        }

                        _stateFilteredProducts.value = Resource.Success(tempFilteredProductList)
                    } else {
                        _stateFilteredProducts.value = Resource.Error(null,responseFromGetFavorites.message!!)
                    }
                } else {
                    _stateFilteredProducts.value = Resource.Error(null,responseFromGetShoppingCartItems.message!!)
                }
            } else {
                _stateFilteredProducts.value = Resource.Error(null,responseFromgetProductsBySearchQuery.message!!)
            }
        }
    }

    fun getFilteredProductsByFilter(userId: String, filter: Filter) {
        _stateFilteredProducts.value = Resource.Loading()
        val tempFilteredProductList = mutableListOf<Product>()

        if (_stateComparedProductIdList.value == null) {
            _stateComparedProductIdList.value = mutableListOf()
        }

        viewModelScope.launch {
            val responseFromgetProductsByFilter = homeRepository.getProductsByFilter(filter)

            if (responseFromgetProductsByFilter is Resource.Success) {
                val responseFromGetShoppingCartItems = homeRepository.getShoppingCartItems(userId)

                if (responseFromGetShoppingCartItems is Resource.Success) {
                    val responseFromGetFavorites = homeRepository.getFavorites(userId)

                    if (responseFromGetFavorites is Resource.Success) {
                        val productEntityList = responseFromgetProductsByFilter.data
                        val shoppingCartItemsEntityList = responseFromGetShoppingCartItems.data
                        val favoritesEntityList = responseFromGetFavorites.data
                        val mutableProductIdList = _stateComparedProductIdList.value!!

                        for (productEntity in productEntityList!!) {
                            var addedToShoppingCart = false
                            var addedToFavorite = false
                            var addedToCompare = false

                            if (shoppingCartItemsEntityList!!.isNotEmpty()) {
                                for (shoppingCartItemEntity in shoppingCartItemsEntityList) {
                                    if (shoppingCartItemEntity.productId == productEntity.productId.toString()) {
                                        addedToShoppingCart = true
                                        break
                                    }
                                }
                            }

                            if (favoritesEntityList!!.isNotEmpty()) {
                                for (favorite in favoritesEntityList) {
                                    if (favorite.productId == productEntity.productId.toString()) {
                                        addedToFavorite = true
                                        break
                                    }
                                }
                            }

                            if (mutableProductIdList.isNotEmpty()) {
                                for (comparedProductId in mutableProductIdList) {
                                    if (comparedProductId == productEntity.productId.toString()) {
                                        addedToCompare = true
                                        break
                                    }
                                }
                            }

                            tempFilteredProductList.add(
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
                                    addedToShoppingCart = addedToShoppingCart,
                                    addedToFavorites = addedToFavorite,
                                    addedToCompare = addedToCompare
                                )
                            )
                        }

                        _stateFilteredProducts.value = Resource.Success(tempFilteredProductList)
                    } else {
                        _stateFilteredProducts.value = Resource.Error(null,responseFromGetFavorites.message!!)
                    }
                } else {
                    _stateFilteredProducts.value = Resource.Error(null,responseFromGetShoppingCartItems.message!!)
                }
            } else {
                _stateFilteredProducts.value = Resource.Error(null,responseFromgetProductsByFilter.message!!)
            }
        }
    }

    fun getFilteredProductsByCategory(userId: String, category: String) {
        _stateFilteredProducts.value = Resource.Loading()
        val tempFilteredProductList = mutableListOf<Product>()

        if (_stateComparedProductIdList.value == null) {
            _stateComparedProductIdList.value = mutableListOf()
        }

        viewModelScope.launch {
            val responseFromgetProductsByFilter = homeRepository.getProductsByCategory(category)

            if (responseFromgetProductsByFilter is Resource.Success) {
                val responseFromGetShoppingCartItems = homeRepository.getShoppingCartItems(userId)

                if (responseFromGetShoppingCartItems is Resource.Success) {
                    val responseFromGetFavorites = homeRepository.getFavorites(userId)

                    if (responseFromGetFavorites is Resource.Success) {
                        val productEntityList = responseFromgetProductsByFilter.data
                        val shoppingCartItemsEntityList = responseFromGetShoppingCartItems.data
                        val favoritesEntityList = responseFromGetFavorites.data
                        val mutableProductIdList = _stateComparedProductIdList.value!!

                        for (productEntity in productEntityList!!) {
                            var addedToShoppingCart = false
                            var addedToFavorite = false
                            var addedToCompare = false

                            if (shoppingCartItemsEntityList!!.isNotEmpty()) {
                                for (shoppingCartItemEntity in shoppingCartItemsEntityList) {
                                    if (shoppingCartItemEntity.productId == productEntity.productId.toString()) {
                                        addedToShoppingCart = true
                                        break
                                    }
                                }
                            }

                            if (favoritesEntityList!!.isNotEmpty()) {
                                for (favorite in favoritesEntityList) {
                                    if (favorite.productId == productEntity.productId.toString()) {
                                        addedToFavorite = true
                                        break
                                    }
                                }
                            }

                            if (mutableProductIdList.isNotEmpty()) {
                                for (comparedProductId in mutableProductIdList) {
                                    if (comparedProductId == productEntity.productId.toString()) {
                                        addedToCompare = true
                                        break
                                    }
                                }
                            }

                            tempFilteredProductList.add(
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
                                    addedToShoppingCart = addedToShoppingCart,
                                    addedToFavorites = addedToFavorite,
                                    addedToCompare = addedToCompare
                                )
                            )
                        }

                        _stateFilteredProducts.value = Resource.Success(tempFilteredProductList)
                    } else {
                        _stateFilteredProducts.value = Resource.Error(null,responseFromGetFavorites.message!!)
                    }
                } else {
                    _stateFilteredProducts.value = Resource.Error(null,responseFromGetShoppingCartItems.message!!)
                }
            } else {
                _stateFilteredProducts.value = Resource.Error(null,responseFromgetProductsByFilter.message!!)
            }
        }
    }

    fun addShoppingCartFilteredProducts(userId: String, productId: String) {
        val tempMutableFilteredProductsList = _stateFilteredProducts.value!!.data
        viewModelScope.launch {
            val responseFromShoppingCart = homeRepository.getShoppingCartByProductId(userId, productId)
            tempMutableFilteredProductsList?.let { products ->
                for (product in products) {
                    if (product.productId == productId) {
                        if (responseFromShoppingCart is Resource.Success) {
                            var isAddedToShoppingCart = responseFromShoppingCart.data!!
                            if (isAddedToShoppingCart) {
                                product.addedToShoppingCart = true
                            } else {
                                val response = homeRepository.insertShoppingCartItems(
                                    ShoppingCartItemsEntity(userId, productId, 1)
                                )
                                if (response is Resource.Success) {
                                    product.addedToShoppingCart = true
                                } else {
                                }
                            }
                        } else {
                        }
                    }
                }
                _stateFilteredProducts.value = Resource.Success(tempMutableFilteredProductsList)
            }

            getShoppingCartCount(userId)
        }
    }

    fun removeFromShoppingCartFilteredProducts(userId: String, productId: String) {
        val tempMutableFilteredProductsList = _stateFilteredProducts.value!!.data
        viewModelScope.launch {
            val response = homeRepository.deleteShoppingCartItem(userId, productId)
            tempMutableFilteredProductsList?.let {products ->
                for (product in products) {
                    if (product.productId == productId) {
                        if (response is Resource.Success) {
                            product.addedToShoppingCart = false
                        } else {
                            val responseFromShoppingCart = homeRepository.getShoppingCartByProductId(userId, productId)
                            if (responseFromShoppingCart is Resource.Success) {
                                product.addedToShoppingCart = responseFromShoppingCart.data!!
                            } else {
                            }
                        }
                    }
                }
                _stateFilteredProducts.value = Resource.Success(tempMutableFilteredProductsList)
            }

            getShoppingCartCount(userId)
        }
    }

    fun addFavoriteFilteredProducts(userId: String, productId: String) {
        val tempMutableFilteredProductsList = _stateFilteredProducts.value!!.data
        viewModelScope.launch {
            val responseFromFavorites = homeRepository.getFavoriteByProductId(userId, productId)
            tempMutableFilteredProductsList?.let {products ->
                for (product in products) {
                    if (product.productId == productId) {
                        var isAddedToFavorite = responseFromFavorites.data!!
                        if (isAddedToFavorite) {
                            product.addedToFavorites = true
                        } else {
                            val response = homeRepository.insertFavorite(FavoritesEntity(userId = userId, productId = productId))
                            if (response is Resource.Success) {
                                product.addedToFavorites = true
                            } else {
                            }
                        }
                    }
                }
                _stateFilteredProducts.value = Resource.Success(tempMutableFilteredProductsList)
            }
        }
    }

    fun removeFromFavoriteFilteredProducts(userId: String, productId: String) {
        val tempMutableFilteredProductsList = _stateFilteredProducts.value!!.data
        viewModelScope.launch {
            val response = homeRepository.deleteFavorite(userId,productId)
            tempMutableFilteredProductsList?.let {products ->
                for (product in products) {
                    if (product.productId == productId) {
                        if (response is Resource.Success) {
                            product.addedToFavorites = false
                        } else {
                            val responseFromFavorites = homeRepository.getFavoriteByProductId(userId, productId)
                            if (responseFromFavorites is Resource.Success) {
                                product.addedToFavorites = responseFromFavorites.data!!
                            } else {
                            }
                        }
                    }
                }
                _stateFilteredProducts.value = Resource.Success(tempMutableFilteredProductsList)
            }
        }
    }

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

    fun clickOnCompareFilteredList(productId: String) {
        val mutableFilteredProductList = _stateFilteredProducts.value!!.data
        if (_stateComparedProductIdList.value == null) {
            _stateComparedProductIdList.value = mutableListOf()
        }

        for (product in mutableFilteredProductList!!) {
            if (productId == product.productId) {
                val isAddedToCompare = !(product.addedToCompare)
                product.addedToCompare = isAddedToCompare

                var mutableProductIdList = _stateComparedProductIdList.value!!
                if (isAddedToCompare) {
                    mutableProductIdList.add(productId)
                } else {
                    mutableProductIdList = mutableProductIdList.filterIndexed { _, s ->
                        productId != s
                    }.toMutableList()
                }
                _stateComparedProductIdList.value = mutableProductIdList
            }
        }

        _stateFilteredProducts.value = Resource.Success(mutableFilteredProductList)

        calculateTotalCompareCountFiltered()
    }

    /// COMPARED PRODUCT

    fun getComparedProductList() {
        if (_stateComparedProductIdList.value == null) {
            _stateComparedProductIdList.value = mutableListOf()
        }
        val mutableProductIdList = _stateComparedProductIdList.value!!
        val products = _stateProducts.value!!.data!!
        val tempComparedProductList = mutableListOf<ComparedProduct>()

        if (mutableProductIdList.isNotEmpty()) {
            for (comparedProductId in mutableProductIdList) {
                for (product in products) {
                    if (comparedProductId == product.productId) {
                        tempComparedProductList.add(
                            ComparedProduct(
                                productId = product.productId,
                                productCategory = product.productCategory,
                                productPhoto = product.productPhoto,
                                productBrand = product.productBrand,
                                productDetail = product.productDetail,
                                productPriceWhole = product.productPriceWhole,
                                productPriceCent = product.productPriceCent,
                                productRate = product.productRate,
                                productReviewCount = product.productReviewCount,
                                productShipping = product.productShipping,
                                productStore = product.productStore,
                                productStoreRate = product.productStoreRate
                            )
                        )
                    }
                }
            }
            _stateComparedProductList.value = tempComparedProductList
        }
    }

    fun removeFromComparedProductList(productId: String) {
        var mutableProductIdList = _stateComparedProductIdList.value!!
        var tempComparedProductList = _stateComparedProductList.value!!

        mutableProductIdList = mutableProductIdList.filterIndexed { _, s ->
            productId != s
        }.toMutableList()

        tempComparedProductList = tempComparedProductList.filterIndexed { _, comparedProduct ->
            productId != comparedProduct.productId
        }.toMutableList()

        _stateComparedProductIdList.value = mutableProductIdList
        _stateComparedProductList.value = tempComparedProductList
    }

    private fun calculateTotalCompareCount() {
        val mutableProductList = _stateProducts.value!!.data
        var compareCount = 0

        for (product in mutableProductList!!) {
            if (product.addedToCompare) {
                compareCount += 1
            }
        }

        _stateCompareCounter.value = compareCount
    }

    private fun calculateTotalCompareCountFiltered() {
        val mutableProductList = _stateFilteredProducts.value!!.data
        var compareCount = 0

        for (product in mutableProductList!!) {
            if (product.addedToCompare) {
                compareCount += 1
            }
        }

        _stateCompareCounter.value = compareCount
    }

     */
}