package com.hasanalic.ecommerce.feature_home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemDto
import com.hasanalic.ecommerce.feature_home.data.local.entity.ShoppingCartItemsEntity

@Dao
interface ShoppingCartItemsDao {

    @Query("""
        SELECT p.*, s.quantity
        FROM Product p
        INNER JOIN ShoppingCartItems s
        ON p.productId = s.product_id
        WHERE s.user_id = :userId
    """)
    suspend fun getProductsInShoppingCart(userId: String): List<ShoppingCartItemDto>?

    @Query("SELECT * FROM ShoppingCartItems WHERE user_id = :userId")
    suspend fun getShoppingCartItems(userId: String): List<ShoppingCartItemsEntity>?

    @Query("SELECT COUNT(*) FROM ShoppingCartItems WHERE user_id = :userId")
    suspend fun getShoppingCartItemCount(userId: String): Int?

    @Query("SELECT shoppingCartEntityId FROM ShoppingCartItems WHERE user_id = :userId AND product_id = :productId")
    suspend fun getShoppingCartEntityByProductId(userId: String, productId: String): Int?

    @Insert
    suspend fun insertShoppingCartItemEntity(shoppingCartItemsEntity: ShoppingCartItemsEntity): Long

    @Insert
    suspend fun insertAllShoppingCartItemEntities(vararg shoppingCartItemsEntities: ShoppingCartItemsEntity): List<Long>

    @Query("UPDATE ShoppingCartItems SET quantity = :quantity " +
            "WHERE user_id = :userId AND product_id = :productId")
    suspend fun updateShoppingCartItemEntity(userId: String, productId: String, quantity: Int): Int

    @Query("DELETE FROM ShoppingCartItems WHERE user_id = :userId AND product_id = :productId")
    suspend fun deleteShoppingCartItemEntity(userId: String, productId: String): Int

    @Query("DELETE FROM ShoppingCartItems WHERE user_id = :userId AND product_id IN (:productIds)")
    suspend fun deleteShoppingCartItemEntitiesByProductIdList(userId: String, productIds: List<String>): Int
}