package com.hasanalic.ecommerce.feature_home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoriteProductDto
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity

@Dao
interface FavoritesDao {

    @Query("""
        SELECT p.*, 
        CASE 
            WHEN sc.product_id IS NOT NULL THEN 1
            ELSE 0
        END as inCart
        FROM Product p
        INNER JOIN Favorites f ON p.productId = f.product_id
        LEFT JOIN ShoppingCartItems sc ON p.productId = sc.product_id AND sc.user_id = :userId
        WHERE f.user_id = :userId
    """)
    suspend fun getFavoriteProducts(userId: String): List<FavoriteProductDto>?

    @Query("SELECT * FROM Favorites WHERE user_id = :userId")
    suspend fun getFavorites(userId: String): List<FavoritesEntity>?

    @Query("SELECT favoriteEntityId FROM Favorites WHERE user_id = :userId AND product_id = :productId")
    suspend fun getFavoriteByProductId(userId: String, productId: String): Int?

    @Insert
    suspend fun insertFavorite(favoritesEntity: FavoritesEntity): Long

    @Query("DELETE FROM Favorites WHERE user_id = :userId AND product_id = :productId")
    suspend fun deleteFavorite(userId: String, productId: String): Int
}