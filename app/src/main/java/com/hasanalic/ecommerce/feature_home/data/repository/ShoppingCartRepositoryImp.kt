package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.feature_home.data.mapper.toShoppingCartItem
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository
import javax.inject.Inject

class ShoppingCartRepositoryImp @Inject constructor(
    private val shoppingCartItemsDao: ShoppingCartItemsDao
) : ShoppingCartRepository {
    override suspend fun getProductsInShoppingCart(userId: String): Result<List<ShoppingCartItem>, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.getProductsInShoppingCart(userId)
            result?.let {
                Result.Success(it.map { shoppingCartItemDto -> shoppingCartItemDto.toShoppingCartItem() })
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getShoppingCartItemCount(userId: String): Result<Int, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.getShoppingCartItemCount(userId)
            result?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertShoppingCartItemEntity(shoppingCartItemsEntity: ShoppingCartItemsEntity): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.insertShoppingCartItemEntity(shoppingCartItemsEntity)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertAllShoppingCartItemEntities(vararg shoppingCartItemEntities: ShoppingCartItemsEntity): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.insertAllShoppingCartItemEntities(*shoppingCartItemEntities)
            if (result.isNotEmpty()) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun updateShoppingCartItemEntity(
        userId: String,
        productId: String,
        quantity: Int
    ): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.updateShoppingCartItemEntity(userId, productId, quantity)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.UPDATE_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteShoppingCartItemEntity(
        userId: String,
        productId: String
    ): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.deleteShoppingCartItemEntity(userId, productId)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.DELETION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteShoppingCartItemEntitiesByProductIdList(
        userId: String,
        productIds: List<String>
    ): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.deleteShoppingCartItemEntitiesByProductIdList(userId, productIds)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.DELETION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun checkIfProductInCart(
        userId: String,
        productId: String
    ): Result<Boolean, DataError.Local> {
        return try {
            val response = shoppingCartItemsDao.getShoppingCartEntityByProductId(userId, productId)
            response?.let {
                Result.Success(true)
            }?: Result.Success(false)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}