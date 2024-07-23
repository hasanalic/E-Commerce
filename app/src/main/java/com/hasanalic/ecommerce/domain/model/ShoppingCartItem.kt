package com.hasanalic.ecommerce.domain.model

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