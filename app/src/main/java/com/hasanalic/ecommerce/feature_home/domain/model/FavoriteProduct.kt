package com.hasanalic.ecommerce.feature_home.domain.model

data class FavoriteProduct(
    val productId: String,
    val category: String,
    val photo: String,
    val brand: String,
    val detail: String,
    val priceWhole: Int,
    val priceCent: Int,
    val rate: Double,
    val reviewCount: String,
    var addedToShoppingCart: Boolean = false
)