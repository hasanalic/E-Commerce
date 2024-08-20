package com.hasanalic.ecommerce.feature_orders.presentation.orders_screen

import com.hasanalic.ecommerce.feature_orders.domain.model.Order

data class OrdersState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val dataError: String? = null
)