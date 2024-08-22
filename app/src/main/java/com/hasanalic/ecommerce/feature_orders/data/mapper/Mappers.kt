package com.hasanalic.ecommerce.feature_orders.data.mapper

import com.hasanalic.ecommerce.feature_orders.data.local.dto.OrderDetailDto
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

fun OrderDetailDto.toOrderDetail() =
    OrderDetail(
        orderId = this.order.orderId.toString(),
        total = this.order.orderTotal,
        productCount = this.order.orderProductCount,
        date = this.order.orderDate,
        status = this.order.orderStatus,
        orderNo = this.order.orderNo,
        cargo = this.order.orderCargo,
        addressTitle = this.address.addressTitle,
        addressDetail = this.address.addressDetail,
        cardName = this.card?.cardName,
        cardNumber = this.card?.cardNumber,
        paymentType = this.order.orderPaymentType,
        timeStamp = this.order.orderTimeStamp,
        time = this.order.orderTime,
        productsList = this.products
    )