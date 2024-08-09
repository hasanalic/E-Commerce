package com.hasanalic.ecommerce.feature_home.data.local.entity

import androidx.room.ColumnInfo

data class ProductDto(
    @ColumnInfo(name = "productId") val productId: Int,
    @ColumnInfo(name = "product_category") val productCategory: String,
    @ColumnInfo(name = "product_photo") val productPhoto: String,
    @ColumnInfo(name = "product_brand") val productBrand: String,
    @ColumnInfo(name = "product_detail") val productDetail: String,
    @ColumnInfo(name = "product_price_whole") val productPriceWhole: Int,
    @ColumnInfo(name = "product_price_cent") val productPriceCent: Int,
    @ColumnInfo(name = "product_rate") val productRate: Double,
    @ColumnInfo(name = "product_review_count") val productReviewCount: String,
    @ColumnInfo(name = "product_barcode") val productBarcode: String,
    @ColumnInfo(name = "product_shipping") val productShipping: String,
    @ColumnInfo(name = "product_store") val productStore: String,
    @ColumnInfo(name = "product_store_rate") val productStoreRate: String,
    val inCart: Boolean,
    val inFavorite: Boolean
)