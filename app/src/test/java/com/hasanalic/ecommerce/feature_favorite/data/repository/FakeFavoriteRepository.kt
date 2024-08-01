package com.hasanalic.ecommerce.feature_favorite.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_favorite.data.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_favorite.domain.repository.FavoriteRepository

class FakeFavoriteRepository : FavoriteRepository {

    private val mutableFavoriteList = mutableListOf(
        FavoritesEntity("1", "2")
    )

    override suspend fun getFavoriteListByUserId(userId: String): Result<List<FavoritesEntity>, DataError.Local> {
        val favoriteList = mutableFavoriteList.filter { it.userId == userId }
        return if (favoriteList.isNotEmpty()) Result.Success(favoriteList) else Result.Error(DataError.Local.NOT_FOUND)
    }

    override suspend fun getFavoriteIdByUserIdAndProductId(
        userId: String,
        productId: String
    ): Result<Int, DataError.Local> {
        val favorite = mutableFavoriteList.find{ it.userId == userId && it.productId == productId }
        return favorite?.let { Result.Success(1) }?: Result.Error(DataError.Local.NOT_FOUND)
    }

    override suspend fun insertFavoriteAndGetId(favoritesEntity: FavoritesEntity): Result<Long, DataError.Local> {
        return Result.Success(1L)
    }

    override suspend fun deleteFavorite(
        userId: String,
        productId: String
    ): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }
}