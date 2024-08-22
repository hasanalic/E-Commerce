package com.hasanalic.ecommerce.feature_orders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
data class OrderEntity(
    @ColumnInfo(name = "order_user_id") var orderUserId: String,
    @ColumnInfo(name = "order_total") var orderTotal: String,
    @ColumnInfo(name = "order_product_count") var orderProductCount: String,
    @ColumnInfo(name = "order_date") var orderDate: String,
    @ColumnInfo(name = "order_status") var orderStatus: String,
    @ColumnInfo(name = "order_no") var orderNo: String,
    @ColumnInfo(name = "order_cargo") var orderCargo: String,
    @ColumnInfo(name = "order_address_id") var orderAddressId: String,
    @ColumnInfo(name = "order_card_id") var orderCardId: String? = null,
    @ColumnInfo(name = "order_payment_type") var orderPaymentType: String,
    @ColumnInfo(name = "order_timestamp") var orderTimeStamp: Long,
    @ColumnInfo(name = "order_time") var orderTime: String
) {
    @PrimaryKey(autoGenerate = true)
    var orderId: Int = 0
}