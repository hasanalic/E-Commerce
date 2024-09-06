package com.hasanalic.ecommerce.feature_home.domain.model

data class Product(
    val productId: String,
    val productCategory: String,
    val productPhoto: String,
    val productBrand: String,
    val productDetail: String,
    val productPriceWhole: Int,
    val productPriceCent: Int,
    val productRate: Double,
    val productReviewCount: String,
    var productBarcode: String,
    var addedToShoppingCart: Boolean,
    var addedToFavorites: Boolean
)