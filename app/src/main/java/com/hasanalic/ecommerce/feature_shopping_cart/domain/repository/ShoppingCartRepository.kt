package com.hasanalic.ecommerce.feature_shopping_cart.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_shopping_cart.data.entity.ShoppingCartItemsEntity
import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem

interface ShoppingCartRepository {

    suspend fun getProductsInShoppingCart(userId: String): Result<List<ShoppingCartItem>, DataError.Local>

    suspend fun getShoppingCartItemCount(userId: String): Result<Int, DataError.Local>

    suspend fun checkShoppingCartEntityByProductId(userId: String, productId: String): Result<Boolean, DataError.Local>

    suspend fun insertShoppingCartItem(shoppingCartItemsEntity: ShoppingCartItemsEntity): Result<Unit, DataError.Local>

    suspend fun insertAllShoppingCartItems(vararg shoppingCartItemsEntities: ShoppingCartItemsEntity): Result<Unit, DataError.Local>

    suspend fun updateShoppingCartItem(userId: String, productId: String, quantity: String): Result<Unit, DataError.Local>

    suspend fun deleteShoppingCartItem(userId: String, productId: String): Result<Unit, DataError.Local>

    suspend fun deleteShoppingCartItemsByProductIdList(userId: String, productIds: List<String>): Result<Unit, DataError.Local>
}