package com.hasanalic.ecommerce.feature_favorite.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_favorite.domain.model.FavoriteProduct

interface FavoriteRepository {

    suspend fun getFavoriteProducts(userId: String): Result<List<FavoriteProduct>, DataError.Local>

    suspend fun getFavoriteListByUserId(userId: String): Result<List<FavoritesEntity>, DataError.Local>

    suspend fun getFavoriteIdByUserIdAndProductId(userId: String, productId: String): Result<Int, DataError.Local>

    suspend fun insertFavoriteAndGetId(favoritesEntity: FavoritesEntity): Result<Long, DataError.Local>

    suspend fun deleteFavorite(userId: String, productId: String): Result<Unit, DataError.Local>
}