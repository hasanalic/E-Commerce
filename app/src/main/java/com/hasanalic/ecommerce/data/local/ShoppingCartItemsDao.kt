package com.hasanalic.ecommerce.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.data.dto.ShoppingCartItemsEntity

@Dao
interface ShoppingCartItemsDao {

    @Query("SELECT * FROM ShoppingCartItems WHERE user_id = :userId")
    suspend fun getShoppingCartItems(userId: String): List<ShoppingCartItemsEntity>?

    @Query("SELECT COUNT(*) FROM ShoppingCartItems WHERE user_id = :userId")
    suspend fun getShoppingCartItemCount(userId: String): Int?

    @Query("SELECT COUNT(*) FROM ShoppingCartItems WHERE user_id = :userId")
    fun getCount(userId: String): Int

    @Query("SELECT shoppingCartEntityId FROM ShoppingCartItems WHERE user_id = :userId AND product_id = :productId")
    suspend fun getShoppingCartByProductId(userId: String, productId: String): Int?

    @Insert
    suspend fun insertShoppingCartItem(shoppingCartItemsEntity: ShoppingCartItemsEntity): Long

    @Insert
    suspend fun insertAllShoppingCartItems(vararg shoppingCartItemsEntities: ShoppingCartItemsEntity): List<Long>

    @Query("UPDATE ShoppingCartItems SET quantity = :quantity " +
            "WHERE user_id = :userId AND product_id = :productId")
    suspend fun updateShoppingCartItem(userId: String, productId: String, quantity: String): Int

    @Query("DELETE FROM ShoppingCartItems WHERE user_id = :userId AND product_id = :productId")
    suspend fun deleteShoppingCartItem(userId: String, productId: String): Int

    @Query("DELETE FROM ShoppingCartItems WHERE user_id = :userId AND product_id IN (:productIds)")
    suspend fun deleteShoppingCartItemsByProductIds(userId: String, productIds: List<String>): Int
}