package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoriteProductDto
import com.hasanalic.ecommerce.feature_home.data.local.entity.FavoritesEntity
import com.hasanalic.ecommerce.feature_home.data.mapper.toFavoriteProduct
import com.hasanalic.ecommerce.feature_home.domain.model.FavoriteProduct
import com.hasanalic.ecommerce.feature_home.domain.repository.FavoriteRepository

class FakeFavoriteRepository : FavoriteRepository {

    private val mutableFavoriteEntityList = mutableListOf(
        FavoritesEntity("1", "1")
    )

    private val mutableFavoriteProductDtoList = mutableListOf(
        FavoriteProductDto(1,"category","url",
            "brand","detail",1,
            1,1.0,"1",
            "321321", "cargo","store",
            "1.1",false)
    )

    override suspend fun getFavoriteProducts(userId: String): Result<List<FavoriteProduct>, DataError.Local> {
        return Result.Success(mutableFavoriteProductDtoList.map { favoriteProductDto -> favoriteProductDto.toFavoriteProduct() })
    }

    override suspend fun getFavoriteListByUserId(userId: String): Result<List<FavoritesEntity>, DataError.Local> {
        val favoriteList = mutableFavoriteEntityList.filter { it.userId == userId }
        return if (favoriteList.isNotEmpty()) Result.Success(favoriteList) else Result.Error(DataError.Local.NOT_FOUND)
    }

    override suspend fun insertFavoriteAndGetId(favoritesEntity: FavoritesEntity): Result<Long, DataError.Local> {
        return Result.Success(1L)
    }

    override suspend fun deleteFavorite(
        userId: String,
        productId: String
    ): Result<Unit, DataError.Local> {
        return if (productId == mutableFavoriteProductDtoList[0].productId.toString()) Result.Success(Unit) else Result.Error(DataError.Local.DELETION_FAILED)
    }

    override suspend fun checkIfProductInFavorites(
        userId: String,
        productId: String
    ): Result<Boolean, DataError.Local> {
        val favoriteEntity = mutableFavoriteEntityList.find { favoriteEntity -> favoriteEntity.productId == productId }
        favoriteEntity?.let {
            return Result.Success(true)
        }?: return Result.Success(false)
    }
}