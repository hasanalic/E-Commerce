package com.hasanalic.ecommerce.feature_checkout.presentation

import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem

object ShoppingCartList {
    var shoppingCartList: List<ShoppingCartItem> = listOf()
    var totalPrice: String = ""
}