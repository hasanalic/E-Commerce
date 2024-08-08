package com.hasanalic.ecommerce.feature_product_detail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.feature_product_detail.data.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor (
    private val homeRepository: HomeRepository
) : ViewModel() {

    private var _stateProduct = MutableLiveData<Resource<Product>>()
    val stateProduct: LiveData<Resource<Product>>
        get() = _stateProduct

    private var _stateReviewList = MutableLiveData<Resource<List<ReviewEntity>>>()
    val stateReviewList: LiveData<Resource<List<ReviewEntity>>>
        get() = _stateReviewList


    fun getProduct(userId: String, productId: String) {
        /*
        _stateProduct.value = Resource.Loading()
        viewModelScope.launch {

            val response = homeRepository.getProductById(productId)
            if (response is Resource.Success) {
                val responseFromShoppingCart = homeRepository.getShoppingCartByProductId(userId, productId)
                val responseFromFavorites = homeRepository.getFavoriteByProductId(userId, productId)
                if (responseFromShoppingCart is Resource.Success && responseFromFavorites is Resource.Success) {
                    val productEntity = response.data
                    _stateProduct.value = Resource.Success(
                        Product(
                            productId = productEntity!!.productId.toString(),
                            productCategory = productEntity.productCategory!!,
                            productPhoto = productEntity.productPhoto!!,
                            productBrand = productEntity.productBrand!!,
                            productDetail = productEntity.productDetail!!,
                            productPriceWhole = productEntity.productPriceWhole!!,
                            productPriceCent = productEntity.productPriceCent!!,
                            productRate = productEntity.productRate!!,
                            productReviewCount = productEntity.productReviewCount!!,
                            productBarcode = productEntity.productBarcode!!,
                            addedToShoppingCart = responseFromShoppingCart.data!!,
                            addedToFavorites = responseFromFavorites.data!!,
                            productShipping = productEntity.productShipping,
                            productStore = productEntity.productStore,
                            productStoreRate = productEntity.productStoreRate
                        )
                    )
                } else {
                    _stateProduct.value = Resource.Error(null,"Beğenilme veya favori durumu alınamadı.")
                }
            } else {
                _stateProduct.value = Resource.Error(null,response.message?:"")
            }
        }

         */
    }

    fun getReviews(productId: String) {
        _stateReviewList.value = Resource.Loading()
        viewModelScope.launch {
            //val response = homeRepository.getReviewsByProductId(productId)
            //_stateReviewList.value = response
        }
    }

    fun addToShoppingCart(userId: String, productId: String) {
        /*
        viewModelScope.launch {
            val response = homeRepository.insertShoppingCartItems(ShoppingCartItemsEntity(userId,productId,1))
            if (response is Resource.Success) {
                val product = _stateProduct.value!!.data
                product!!.addedToShoppingCart = true
                _stateProduct.value = Resource.Success(product)
            } else {
                _stateProduct.value = Resource.Error(null,"Alışveriş sepetine eklenemedi.")
            }
        }

         */
    }

    fun removeFromShoppingCart(userId: String, productId: String) {
        /*
        viewModelScope.launch {
            val response = homeRepository.deleteShoppingCartItem(userId,productId)
            if (response is Resource.Success) {
                val product = _stateProduct.value!!.data
                product!!.addedToShoppingCart = false
                _stateProduct.value = Resource.Success(product)
            } else {
                _stateProduct.value = Resource.Error(null,"Alışveriş sepetinden silinemedi.")
            }
        }

         */
    }

    fun addToFavorites(userId: String, productId: String) {
        /*
        viewModelScope.launch {
            val response = homeRepository.insertFavorite(FavoritesEntity(userId,productId))
            if (response is Resource.Success) {
                val product = _stateProduct.value!!.data
                product!!.addedToFavorites = true
                _stateProduct.value = Resource.Success(product)
            } else {
                _stateProduct.value = Resource.Error(null,"Favorilere eklenemedi.")
            }
        }

         */
    }

    fun removeFromFavorites(userId: String, productId: String) {
        /*
        viewModelScope.launch {
            val response = homeRepository.deleteFavorite(userId,productId)
            if (response is Resource.Success) {
                val product = _stateProduct.value!!.data
                product!!.addedToFavorites = false
                _stateProduct.value = Resource.Success(product)
            } else {
                _stateProduct.value = Resource.Error(null,"Favorilerden kaldırılamadı.")
            }
        }i

         */
    }
}