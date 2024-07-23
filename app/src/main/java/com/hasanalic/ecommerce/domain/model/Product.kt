package com.hasanalic.ecommerce.domain.model

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
    var addedToShoppingCart: Boolean = false,
    var addedToFavorites: Boolean = false,
    var addedToCompare: Boolean = false,
    var productShipping: String? = null,
    var productStore: String? = null,
    var productStoreRate: String? = null
)