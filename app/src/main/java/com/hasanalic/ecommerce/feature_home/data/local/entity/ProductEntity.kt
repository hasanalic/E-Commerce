package com.hasanalic.ecommerce.feature_home.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Product")
data class ProductEntity(
    @ColumnInfo(name = "product_category") var productCategory: String,
    @ColumnInfo(name = "product_photo") var productPhoto: String,
    @ColumnInfo(name = "product_brand") var productBrand: String,
    @ColumnInfo(name = "product_detail") var productDetail: String,
    @ColumnInfo(name = "product_price_whole") var productPriceWhole: Int,
    @ColumnInfo(name = "product_price_cent") var productPriceCent: Int,
    @ColumnInfo(name = "product_rate") var productRate: Double,
    @ColumnInfo(name = "product_review_count") var productReviewCount: String,
    @ColumnInfo(name = "product_barcode") var productBarcode: String,
    @ColumnInfo(name = "product_shipping") var productShipping: String,
    @ColumnInfo(name = "product_store") var productStore: String,
    @ColumnInfo(name = "product_store_rate") var productStoreRate: String,
) {
    @PrimaryKey(autoGenerate = true)
    var productId: Int = 0
}
