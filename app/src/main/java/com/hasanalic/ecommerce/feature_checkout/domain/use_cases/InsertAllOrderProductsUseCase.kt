package com.hasanalic.ecommerce.feature_checkout.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.domain.repository.OrderProductsRepository
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import javax.inject.Inject

class InsertAllOrderProductsUseCase @Inject constructor(
    private val orderProductsRepository: OrderProductsRepository
) {
    suspend operator fun invoke(vararg orderProductsEntity: OrderProductsEntity): Result<Unit, DataError.Local> {
        return orderProductsRepository.insertAllOrderProducts(*orderProductsEntity)
    }
}