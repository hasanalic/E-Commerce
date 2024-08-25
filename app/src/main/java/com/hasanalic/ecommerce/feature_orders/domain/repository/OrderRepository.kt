package com.hasanalic.ecommerce.feature_orders.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail

interface OrderRepository {

    suspend fun insertOrder(order: OrderEntity): Result<Long, DataError.Local>

    suspend fun getOrderDetail(orderId: String): Result<OrderDetail, DataError.Local>

    suspend fun getOrdersByUser(userId: String): Result<List<Order>, DataError.Local>

    suspend fun updateOrderStatus(newStatus: String, userId: String, orderId: String): Result<Unit, DataError.Local>


    /*

    suspend fun getAddressByUserIdAndAddressId(userId: String, addressId: String): Resource<AddressEntity>

    suspend fun getOrderProductsList(userId: String, orderId: String): Resource<List<OrderProductsEntity>>

    suspend fun getCardByUserId(userId: String, paymentId: String): Resource<PaymentEntity>

     */


    /*
    suspend fun getOrdersByUserId(userId: String): Resource<List<OrderEntity>>

    suspend fun getOrder(userId: String, orderId: String): Resource<OrderEntity>
     */
}