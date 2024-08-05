package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen

import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem

data class ShoppingCartState(
    val isLoading: Boolean = false,
    val shoppingCartItemList: MutableList<ShoppingCartItem> = mutableListOf(),
    val totalPriceWhole: Int? = null,
    val totalPriceCent: Int? = null,
    val dataError: String? = null,
    val actionError: String? = null
)