package com.hasanalic.ecommerce.ui.order

import com.hasanalic.ecommerce.domain.model.Order

class OrderSingleton {
    companion object {
        var order: Order? = null
    }
}