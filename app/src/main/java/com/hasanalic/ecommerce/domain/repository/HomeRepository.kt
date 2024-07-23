package com.hasanalic.ecommerce.domain.repository

import com.hasanalic.ecommerce.data.dto.FavoritesEntity
import com.hasanalic.ecommerce.data.dto.ProductEntity
import com.hasanalic.ecommerce.data.dto.ReviewEntity
import com.hasanalic.ecommerce.data.dto.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.domain.model.Chip
import com.hasanalic.ecommerce.ui.filter.Filter
import com.hasanalic.ecommerce.utils.Resource

interface HomeRepository {

    suspend fun getProducts(): Resource<List<ProductEntity>>

    suspend fun insertProducts(vararg products: ProductEntity): Resource<Boolean>

    suspend fun getShoppingCartItems(userId: String): Resource<List<ShoppingCartItemsEntity>>

    suspend fun getShoppingCartCount(userId: String): Resource<Int>

    suspend fun insertShoppingCartItems(shoppingCartItemsEntity: ShoppingCartItemsEntity): Resource<Boolean>

    suspend fun insertAllShoppingCartItems(vararg shoppingCartItemsEntities: ShoppingCartItemsEntity): Resource<Boolean>

    suspend fun updateShoppingCartItem(userId: String, productId: String, quantity: String): Resource<Boolean>

    suspend fun deleteShoppingCartItem(userId: String, productId: String): Resource<Boolean>

    suspend fun getFavorites(userId: String): Resource<List<FavoritesEntity>>

    suspend fun insertFavorite(favoritesEntity: FavoritesEntity): Resource<Boolean>

    suspend fun deleteFavorite(userId: String, productId: String): Resource<Boolean>

    suspend fun getFavoriteProducts(favoriteProductIds: List<String>): Resource<List<ProductEntity>>

    suspend fun getProductsBySearchQuery(searchQuery: String): Resource<List<ProductEntity>>

    suspend fun getProductsByCategory(productCategory: String): Resource<List<ProductEntity>>

    suspend fun getProductIdByBarcode(productBarcode: String): Resource<Int>

    suspend fun getProductById(productId: String): Resource<ProductEntity>

    suspend fun getReviewsByProductId(productId: String): Resource<List<ReviewEntity>>

    suspend fun insertAllReviews(vararg reviews: ReviewEntity): Resource<Boolean>

    suspend fun getFavoriteByProductId(userId: String, productId: String): Resource<Boolean>

    suspend fun getShoppingCartByProductId(userId: String, productId: String): Resource<Boolean>

    suspend fun getProductsByFilter(filter: Filter): Resource<List<ProductEntity>>

    suspend fun getShoppingCartItemCount(userId: String): Resource<Int>

    suspend fun getCategories(): Resource<List<Chip>>

    suspend fun getBrands(): Resource<List<Chip>>

    suspend fun getBrandsByCategory(category: String): Resource<List<Chip>>
}