package com.hasanalic.ecommerce.feature_shopping_cart.data.entity

data class ShoppingCartItemDto(
    val productId: Int,
    val productCategory: String,
    val productPhoto: String,
    val productBrand: String,
    val productDetail: String,
    val productPriceWhole: Int,
    val productPriceCent: Int,
    val productRate: Double,
    val productReviewCount: String,
    val productBarcode: String,
    val productShipping: String,
    val productStore: String,
    val productStoreRate: String,
    val quantity: Int
)