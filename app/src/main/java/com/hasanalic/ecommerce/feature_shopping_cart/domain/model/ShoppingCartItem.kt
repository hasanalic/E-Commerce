package com.hasanalic.ecommerce.feature_shopping_cart.domain.model

data class ShoppingCartItem(
    val shoppingCartItemId: String,
    val shoppingCartItemCategory: String,
    val shoppingCartItemPhoto: String,
    val shoppingCartItemBrand: String,
    val shoppingCartItemDetail: String,
    val shoppingCartItemPriceWhole: Int,
    val shoppingCartItemPriceCent: Int,
    var shoppingCartItemQuantity: Int
)