package com.hasanalic.ecommerce.feature_home.presentation.shopping_cart_screen

import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_home.domain.model.ShoppingCartItem

data class ShoppingCartState(
    val isLoading: Boolean = false,
    val shoppingCartItemList: MutableList<ShoppingCartItem> = mutableListOf(),
    val totalPriceWhole: Int? = null,
    val totalPriceCent: Int? = null,
    val isUserLoggedIn: Boolean = false,
    val canUserMoveToCheckout: Boolean = false,
    val userId: String = ANOMIM_USER_ID,
    val shouldUserMoveToAuthActivity: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null
)