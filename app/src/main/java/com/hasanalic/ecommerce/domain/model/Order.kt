package com.hasanalic.ecommerce.domain.model

import com.hasanalic.ecommerce.data.dto.OrderProductsEntity

data class Order(
    var orderId: String,
    var orderUserId: String,
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