package com.hasanalic.ecommerce.feature_favorite.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoritesEntity

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM Favorites WHERE user_id = :userId")
    suspend fun getFavorites(userId: String): List<FavoritesEntity>?

    @Query("SELECT favoriteEntityId FROM Favorites WHERE user_id = :userId AND product_id = :productId")
    suspend fun getFavoriteByProductId(userId: String, productId: String): Int?

    @Insert
    suspend fun insertFavorite(favoritesEntity: FavoritesEntity): Long

    @Query("DELETE FROM Favorites WHERE user_id = :userId AND product_id = :productId")
    suspend fun deleteFavorite(userId: String, productId: String): Int
}