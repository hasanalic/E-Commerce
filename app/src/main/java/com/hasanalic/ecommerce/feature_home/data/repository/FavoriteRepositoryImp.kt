package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.data.local.FavoritesDao
import com.hasanalic.ecommerce.feature_home.domain.model.FavoriteProduct
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository
import com.hasanalic.ecommerce.feature_home.data.mapper.toFavoriteProduct
import javax.inject.Inject

class FavoriteRepositoryImp @Inject constructor(
    private val favoritesDao: FavoritesDao,
) : FavoriteRepository {
    override suspend fun getFavoriteProducts(userId: String): Result<List<FavoriteProduct>, DataError.Local> {
        return try {
            val result = favoritesDao.getFavoriteProducts(userId)
            result?.let {
                Result.Success(result.map { it.toFavoriteProduct() })
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getFavoriteListByUserId(userId: String): Result<List<FavoritesEntity>, DataError.Local> {
        return try {
            val favoriteList = favoritesDao.getFavorites(userId)
            favoriteList?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertFavoriteAndGetId(favoritesEntity: FavoritesEntity): Result<Long, DataError.Local> {
        return try {
            val favoriteId = favoritesDao.insertFavorite(favoritesEntity)
            if (favoriteId > 0) {
                Result.Success(favoriteId)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteFavorite(
        userId: String,
        productId: String
    ): Result<Unit, DataError.Local> {
        return try {
            val affectedRowCount = favoritesDao.deleteFavorite(userId, productId)
            if (affectedRowCount > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.DELETION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun checkIfProductInFavorites(
        userId: String,
        productId: String
    ): Result<Boolean, DataError.Local> {
        return try {
            val result = favoritesDao.getFavoriteByProductId(userId, productId)
            result?.let {
                Result.Success(true)
            }?: Result.Success(false)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}