package com.hasanalic.ecommerce.feature_orders.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderEntity

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long?

    @Query("SELECT * FROM `Order` WHERE order_user_id = :userId ORDER BY order_timestamp DESC")
    suspend fun getOrdersByUserId(userId: String): List<OrderEntity>?

    @Query("SELECT * FROM `Order` WHERE order_user_id = :userId AND orderId = :orderId")
    suspend fun getOrder(userId: String, orderId: String): OrderEntity?

    @Query("UPDATE `Order` SET order_status = :newStatus WHERE order_user_id = :userId AND orderId = :orderId")
    suspend fun updateOrderStatus(newStatus: String, userId: String, orderId: String): Int
}