package com.hasanalic.ecommerce.feature_shopping_cart.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_shopping_cart.data.local.ShoppingCartItemsDao
import com.hasanalic.ecommerce.feature_shopping_cart.data.mapper.toShoppingCartItem
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_shopping_cart.domain.repository.ShoppingCartRepository
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

    override suspend fun checkShoppingCartEntityByProductId(
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

    override suspend fun insertShoppingCartItem(shoppingCartItemsEntity: ShoppingCartItemsEntity): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.insertShoppingCartItem(shoppingCartItemsEntity)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILD)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertAllShoppingCartItems(vararg shoppingCartItemsEntities: ShoppingCartItemsEntity): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.insertAllShoppingCartItems(*shoppingCartItemsEntities)
            if (result.isNotEmpty()) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILD)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun updateShoppingCartItem(
        userId: String,
        productId: String,
        quantity: String
    ): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.updateShoppingCartItem(userId, productId, quantity)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.UPDATE_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteShoppingCartItem(
        userId: String,
        productId: String
    ): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.deleteShoppingCartItem(userId, productId)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.DELETION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteShoppingCartItemsByProductIdList(
        userId: String,
        productIds: List<String>
    ): Result<Unit, DataError.Local> {
        return try {
            val result = shoppingCartItemsDao.deleteShoppingCartItemsByProductIdList(userId, productIds)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.DELETION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}