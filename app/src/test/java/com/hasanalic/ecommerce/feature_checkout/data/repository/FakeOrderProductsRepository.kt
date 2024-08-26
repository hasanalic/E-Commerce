package com.hasanalic.ecommerce.feature_checkout.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.domain.repository.OrderProductsRepository
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity

class FakeOrderProductsRepository: OrderProductsRepository {
    override suspend fun insertAllOrderProducts(vararg orderProductsEntity: OrderProductsEntity): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }
}