package com.hasanalic.ecommerce.feature_orders.presentation

import com.hasanalic.ecommerce.domain.model.Order

class OrderSingleton {
    companion object {
        var order: Order? = null
    }
}