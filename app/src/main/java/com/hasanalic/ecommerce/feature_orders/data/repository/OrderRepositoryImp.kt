package com.hasanalic.ecommerce.feature_orders.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.OrderDao
import com.hasanalic.ecommerce.feature_orders.data.mapper.toOrder
import com.hasanalic.ecommerce.feature_orders.data.mapper.toOrderDetail
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImp @Inject constructor(
    private val orderDao: OrderDao
): OrderRepository {
    override suspend fun insertOrder(order: OrderEntity): Result<Long, DataError.Local> {
        return try {
            val result = orderDao.insertOrder(order)
            if (result > 0) {
                Result.Success(result)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getOrderDetail(orderId: String): Result<OrderDetail, DataError.Local> {
        return try {
            val result = orderDao.getOrderDetail(orderId)
            result?.let {
                Result.Success(result.toOrderDetail())
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getOrdersByUser(userId: String): Result<List<Order>, DataError.Local> {
        return try {
            val result = orderDao.getOrdersWithProductsByUser(userId)
            result?.let {
                Result.Success(result.map { it.toOrder() })
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun updateOrderStatus(newStatus: String, userId: String, orderId: String): Result<Unit, DataError.Local> {
        return try {
            val result = orderDao.updateOrderStatus(newStatus, userId, orderId)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.UPDATE_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}