package com.hasanalic.ecommerce.domain.repository

import com.hasanalic.ecommerce.data.dto.AddressEntity
import com.hasanalic.ecommerce.data.dto.OrderEntity
import com.hasanalic.ecommerce.data.dto.OrderProductsEntity
import com.hasanalic.ecommerce.data.dto.PaymentEntity
import com.hasanalic.ecommerce.utils.Resource

interface OrderRepository {
    suspend fun getAddressByUserIdAndAddressId(userId: String, addressId: String): Resource<AddressEntity>

    suspend fun getOrdersByUserId(userId: String): Resource<List<OrderEntity>>

    suspend fun getOrder(userId: String, orderId: String): Resource<OrderEntity>

    suspend fun updateOrderStatus(newStatus: String, userId: String, orderId: String): Resource<Boolean>

    suspend fun getOrderProductsList(userId: String, orderId: String): Resource<List<OrderProductsEntity>>

    suspend fun getCardByUserId(userId: String, paymentId: String): Resource<PaymentEntity>
}