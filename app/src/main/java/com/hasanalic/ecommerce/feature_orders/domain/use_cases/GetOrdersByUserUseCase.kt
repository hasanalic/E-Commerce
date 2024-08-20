package com.hasanalic.ecommerce.feature_orders.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersByUserUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Order>, DataError.Local> {
        return orderRepository.getOrdersByUser(userId)
    }
}