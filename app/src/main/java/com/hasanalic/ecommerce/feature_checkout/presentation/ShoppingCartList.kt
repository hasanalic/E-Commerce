package com.hasanalic.ecommerce.feature_checkout.presentation

import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem

object ShoppingCartList {
    var shoppingCartList: List<ShoppingCartItem> = listOf()
    var totalPrice: String = ""
}