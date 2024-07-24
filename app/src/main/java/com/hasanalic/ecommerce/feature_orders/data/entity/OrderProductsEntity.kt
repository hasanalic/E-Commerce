package com.hasanalic.ecommerce.feature_orders.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OrderProducts")
data class OrderProductsEntity(
    @ColumnInfo(name = "order_products_user_id") var orderProductsUserId: String? = null,
    @ColumnInfo(name = "order_products_order_id") var orderProductsOrderId: String? = null,
    @ColumnInfo(name = "order_products_product_id") var orderProductsProductId: String? = null,
    @ColumnInfo(name = "order_products_product_quantity") var orderProductsProductQuantity: String? = null,
    @ColumnInfo(name = "order_products_product_image") var orderProductsProductImage: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var orderProductsId: Int = 0
}
