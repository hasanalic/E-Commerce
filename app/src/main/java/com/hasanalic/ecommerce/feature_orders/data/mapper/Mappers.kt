package com.hasanalic.ecommerce.feature_orders.data.mapper

import com.hasanalic.ecommerce.feature_orders.data.local.dto.OrderWithProductsDto
import com.hasanalic.ecommerce.feature_orders.domain.model.Order
import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail

fun OrderWithProductsDto.toOrder() =
    Order(
        orderId = this.order.orderId.toString(),
        orderUserId = this.order.orderUserId,
        orderTotal = this.order.orderTotal,
        orderProductCount = this.order.orderProductCount,
        orderDate = this.order.orderDate,
        orderStatus = this.order.orderStatus,
        orderTimeStamp = this.order.orderTimeStamp,
        orderTime = this.order.orderTime,
        orderProductsList = this.products
    )

fun OrderWithProductsDto.toOrderDetail() =
    OrderDetail(
        orderId = this.order.orderId.toString(),
        orderTotal = this.order.orderTotal,
        orderProductCount = this.order.orderProductCount,
        orderDate = this.order.orderDate,
        orderStatus = this.order.orderStatus,
        orderNo = this.order.orderNo,
        orderCargo = this.order.orderCargo,
        orderAddressId = this.order.orderAddressId,
        orderPaymentId = this.order.orderPaymentId,
        orderPaymentType = this.order.orderPaymentType,
        orderTimeStamp = this.order.orderTimeStamp,
        orderTime = this.order.orderTime,
        orderProductsList = this.products
    )