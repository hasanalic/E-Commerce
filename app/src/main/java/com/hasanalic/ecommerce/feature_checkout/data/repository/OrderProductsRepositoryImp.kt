package com.hasanalic.ecommerce.feature_checkout.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.data.local.OrderProductsDao
import com.hasanalic.ecommerce.feature_checkout.domain.repository.OrderProductsRepository
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import javax.inject.Inject

class OrderProductsRepositoryImp @Inject constructor(
    private val orderProductsDao: OrderProductsDao
) : OrderProductsRepository {
    override suspend fun insertAllOrderProducts(vararg orderProductsEntity: OrderProductsEntity): Result<Unit, DataError.Local> {
        return try {
            val result = orderProductsDao.insertAllOrderProducts(*orderProductsEntity)
            if (orderProductsEntity.size == result.size) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.NOT_FOUND)
        }
    }
}