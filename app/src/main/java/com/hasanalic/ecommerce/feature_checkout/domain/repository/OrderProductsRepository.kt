package com.hasanalic.ecommerce.feature_checkout.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity

interface OrderProductsRepository {
    suspend fun insertAllOrderProducts(vararg orderProductsEntity: OrderProductsEntity): Result<Unit, DataError.Local>
}