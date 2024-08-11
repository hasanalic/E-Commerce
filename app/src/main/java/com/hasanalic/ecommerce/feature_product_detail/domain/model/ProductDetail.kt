package com.hasanalic.ecommerce.feature_product_detail.domain.model

data class ProductDetail(
    val productId: String,
    val productCategory: String,
    val productPhoto: String,
    val productBrand: String,
    val productDetail: String,
    val productPriceWhole: Int,
    val productPriceCent: Int,
    val productRate: Double,
    val productReviewCount: String,
    val productShipping: String,
    val productStore: String,
    val productStoreRate: String,
    var addedToShoppingCart: Boolean,
    var addedToFavorites: Boolean,
)