package com.hasanalic.ecommerce.feature_checkout.domain.repository

import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import com.hasanalic.ecommerce.utils.Resource

interface CheckoutRepository {
    suspend fun insertOrder(orderEntity: OrderEntity): Resource<Long>

    suspend fun insertAllOrderProducts(vararg orderProductsEntity: OrderProductsEntity): Resource<Boolean>

    suspend fun deleteShoppingCartItemsByProductIds(userId: String, productIds: List<String>): Resource<Boolean>
}