package com.hasanalic.ecommerce.feature_orders.domain.model

import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity

data class OrderDetail(
    var orderId: String,
    var orderTotal: String,
    var orderProductCount: String,
    var orderDate: String,
    var orderStatus: String,
    var orderNo: String,
    var orderCargo: String,
    var orderAddressId: String,
    var orderPaymentId: String? = null,
    var orderPaymentType: String,
    var orderTimeStamp: Long,
    var orderTime: String,
    var orderProductsList: List<OrderProductsEntity>
)