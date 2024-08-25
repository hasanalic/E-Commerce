package com.hasanalic.ecommerce.feature_order.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderEntity
import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail
import com.hasanalic.ecommerce.feature_orders.domain.repository.OrderRepository

class FakeOrderRepository : OrderRepository{

    private val orderProductsEntity = OrderProductsEntity("1","1","1","1","url")
    private val orderDetail = OrderDetail("1","","","","","","","","","","","",1L,"", listOf(orderProductsEntity))
    private val orders = listOf(
        Order("1","1","","","","",1L,"", listOf(orderProductsEntity))
    )

    override suspend fun insertOrder(order: OrderEntity): Result<Long, DataError.Local> {
        return Result.Success(1L)
    }

    override suspend fun getOrderDetail(orderId: String): Result<OrderDetail, DataError.Local> {
        return Result.Success(orderDetail)
    }

    override suspend fun getOrdersByUser(userId: String): Result<List<Order>, DataError.Local> {
        return Result.Success(orders)
    }

    override suspend fun updateOrderStatus(
        newStatus: String,
        userId: String,
        orderId: String
    ): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }
}