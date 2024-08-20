package com.hasanalic.ecommerce.feature_orders.domain.model

import com.hasanalic.ecommerce.feature_orders.data.local.entity.OrderProductsEntity

data class OrderDetail(
    var orderId: String,
    var total: String,
    var productCount: String,
    var date: String,
    var status: String,
    var orderNo: String,
    var cargo: String,
    var addressTitle: String,
    var addressDetail: String,
    var cardName: String? = null,
    var cardNumber: String? = null,
    var paymentType: String,
    var timeStamp: Long,
    var time: String,
    var productsList: List<OrderProductsEntity>
)