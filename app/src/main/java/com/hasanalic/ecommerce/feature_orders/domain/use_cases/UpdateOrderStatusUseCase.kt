package com.hasanalic.ecommerce.feature_orders.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(newStatus: String, userId: String, orderId: String): Result<Unit, DataError.Local> {
        return orderRepository.updateOrderStatus(newStatus, userId, orderId)
    }
}