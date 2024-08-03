package com.hasanalic.ecommerce.feature_shopping_cart.domain.model

data class ShoppingCartItem(
    val productId: String,
    val category: String,
    val photo: String,
    val brand: String,
    val detail: String,
    val priceWhole: Int,
    val priceCent: Int,
    var quantity: Int
)