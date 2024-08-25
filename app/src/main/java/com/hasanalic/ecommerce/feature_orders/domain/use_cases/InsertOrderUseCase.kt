package com.hasanalic.ecommerce.feature_orders.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import javax.inject.Inject

class InsertOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(order: OrderEntity): Result<Long, DataError.Local> {
        return orderRepository.insertOrder(order)
    }
}