package com.hasanalic.ecommerce.feature_orders.presentation

import com.hasanalic.ecommerce.feature_orders.domain.model.Order

class OrderSingleton {
    companion object {
        var order: Order? = null
    }
}