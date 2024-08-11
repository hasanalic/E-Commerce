package com.hasanalic.ecommerce.feature_orders.domain.repository

import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.PaymentEntity
import com.hasanalic.ecommerce.utils.Resource

interface OrderRepository {
    suspend fun getAddressByUserIdAndAddressId(userId: String, addressId: String): Resource<AddressEntity>

    suspend fun getOrdersByUserId(userId: String): Resource<List<OrderEntity>>

    suspend fun getOrder(userId: String, orderId: String): Resource<OrderEntity>

    suspend fun updateOrderStatus(newStatus: String, userId: String, orderId: String): Resource<Boolean>

    suspend fun getOrderProductsList(userId: String, orderId: String): Resource<List<OrderProductsEntity>>

    suspend fun getCardByUserId(userId: String, paymentId: String): Resource<PaymentEntity>
}