package com.hasanalic.ecommerce.feature_home.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem

interface ShoppingCartRepository {

    suspend fun getProductsInShoppingCart(userId: String): Result<List<ShoppingCartItem>, DataError.Local>

    suspend fun getShoppingCartItemCount(userId: String): Result<Int, DataError.Local>

    suspend fun insertShoppingCartItemEntity(shoppingCartItemsEntity: ShoppingCartItemsEntity): Result<Unit, DataError.Local>

    suspend fun insertAllShoppingCartItemEntities(vararg shoppingCartItemEntities: ShoppingCartItemsEntity): Result<Unit, DataError.Local>

    suspend fun updateShoppingCartItemEntity(userId: String, productId: String, quantity: Int): Result<Unit, DataError.Local>

    suspend fun deleteShoppingCartItemEntity(userId: String, productId: String): Result<Unit, DataError.Local>

    suspend fun deleteShoppingCartItemEntitiesByProductIdList(userId: String, productIds: List<String>): Result<Unit, DataError.Local>

    suspend fun checkIfProductInCart(userId: String, productId: String): Result<Boolean, DataError.Local>
}