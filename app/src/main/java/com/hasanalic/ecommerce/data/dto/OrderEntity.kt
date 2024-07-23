package com.hasanalic.ecommerce.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Order")
data class OrderEntity(
    @ColumnInfo(name = "order_user_id") var orderUserId: String? = null,
    @ColumnInfo(name = "order_total") var orderTotal: String? = null,
    @ColumnInfo(name = "order_product_count") var orderProductCount: String? = null,
    @ColumnInfo(name = "order_date") var orderDate: String? = null,
    @ColumnInfo(name = "order_status") var orderStatus: String? = null,
    @ColumnInfo(name = "order_no") var orderNo: String? = null,
    @ColumnInfo(name = "order_cargo") var orderCargo: String? = null,
    @ColumnInfo(name = "order_address_id") var orderAddressId: String? = null,
    @ColumnInfo(name = "order_payment_id") var orderPaymentId: String? = null,
    @ColumnInfo(name = "order_payment_type") var orderPaymentType: String? = null,
    @ColumnInfo(name = "order_timestamp") var orderTimeStamp: Long? = null,
    @ColumnInfo(name = "order_time") var orderTime: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var orderId: Int = 0
}
