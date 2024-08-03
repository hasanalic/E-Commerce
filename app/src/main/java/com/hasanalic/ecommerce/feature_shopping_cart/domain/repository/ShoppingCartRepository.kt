package com.hasanalic.ecommerce.feature_shopping_cart.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem

interface ShoppingCartRepository {

    suspend fun getProductsInShoppingCart(userId: String): Result<List<ShoppingCartItem>, DataError.Local>

    suspend fun getShoppingCartItemCount(userId: String): Result<Int, DataError.Local>

    suspend fun checkShoppingCartEntityByProductId(userId: String, productId: String): Result<Boolean, DataError.Local>

    suspend fun insertShoppingCartItemEntity(shoppingCartItemsEntity: ShoppingCartItemsEntity): Result<Unit, DataError.Local>

    suspend fun insertAllShoppingCartItemEntities(vararg shoppingCartItemEntities: ShoppingCartItemsEntity): Result<Unit, DataError.Local>

    suspend fun updateShoppingCartItemEntity(userId: String, productId: String, quantity: String): Result<Unit, DataError.Local>

    suspend fun deleteShoppingCartItemEntity(userId: String, productId: String): Result<Unit, DataError.Local>

    suspend fun deleteShoppingCartItemEntitiesByProductIdList(userId: String, productIds: List<String>): Result<Unit, DataError.Local>
}