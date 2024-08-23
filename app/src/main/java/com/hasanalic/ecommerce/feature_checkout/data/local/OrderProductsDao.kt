package com.hasanalic.ecommerce.feature_checkout.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity

@Dao
interface OrderProductsDao {
    @Insert
    suspend fun insertAllOrderProducts(vararg orderProductsEntity: OrderProductsEntity): List<Long>

    @Query("SELECT * FROM OrderProducts WHERE order_products_user_id = :userId AND order_products_order_id = :orderId")
    suspend fun getOrderProductsList(userId: String, orderId: String): List<OrderProductsEntity>?
}