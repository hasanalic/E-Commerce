package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem
import com.hasanalic.ecommerce.feature_home.domain.repository.ShoppingCartRepository

class FakeShoppingCartRepository : ShoppingCartRepository {

    private val shoppingCartItemList = mutableListOf(
        ShoppingCartItem("1","category","photo","brand","detail",1,1,2)
    )

    override suspend fun getProductsInShoppingCart(userId: String): Result<List<ShoppingCartItem>, DataError.Local> {
        return if (userId == "1") Result.Success(shoppingCartItemList) else Result.Error(DataError.Local.NOT_FOUND)
    }

    override suspend fun getShoppingCartItemCount(userId: String): Result<Int, DataError.Local> {
        return Result.Success(shoppingCartItemList.size)
    }

    override suspend fun insertShoppingCartItemEntity(shoppingCartItemsEntity: ShoppingCartItemsEntity): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }

    override suspend fun insertAllShoppingCartItemEntities(vararg shoppingCartItemEntities: ShoppingCartItemsEntity): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }

    override suspend fun updateShoppingCartItemEntity(
        userId: String,
        productId: String,
        quantity: Int
    ): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }

    override suspend fun deleteShoppingCartItemEntity(
        userId: String,
        productId: String
    ): Result<Unit, DataError.Local> {
        return if (productId == shoppingCartItemList[0].productId) Result.Success(Unit) else Result.Error(DataError.Local.DELETION_FAILED)
    }

    override suspend fun deleteShoppingCartItemEntitiesByProductIdList(
        userId: String,
        productIds: List<String>
    ): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }

    override suspend fun checkIfProductInCart(
        userId: String,
        productId: String
    ): Result<Boolean, DataError.Local> {
        val shoppingCartItem = shoppingCartItemList.find { shoppingCartItem -> shoppingCartItem.productId == productId }
        shoppingCartItem?.let {
            return Result.Success(true)
        }?: return Result.Success(false)
    }
}