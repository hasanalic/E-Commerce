package com.hasanalic.ecommerce.feature_orders.presentation.order_detail_screen

import com.hasanalic.ecommerce.feature_orders.domain.model.OrderDetail

data class OrderDetailState(
    val isLoading: Boolean = false,
    val orderDetail: OrderDetail? = null,
    val isOrderStatusUpdateSuccessful: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null
)