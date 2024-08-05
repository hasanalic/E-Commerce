package com.hasanalic.ecommerce.feature_shopping_cart.presentation

import com.hasanalic.ecommerce.feature_shopping_cart.domain.model.ShoppingCartItem

data class ShoppingCartState(
    val isLoading: Boolean = false,
    val shoppingCartItemList: MutableList<ShoppingCartItem> = mutableListOf(),
    val totalPriceWhole: Int? = null,
    val totalPriceCent: Int? = null,
    val dataError: String? = null,
    val actionError: String? = null
)