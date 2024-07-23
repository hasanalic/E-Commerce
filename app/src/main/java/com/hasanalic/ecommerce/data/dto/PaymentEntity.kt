package com.hasanalic.ecommerce.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Payment")
data class PaymentEntity(
    @ColumnInfo(name = "payment_card_name") var paymentCardName: String? = null,
    @ColumnInfo(name = "payment_card_number") var paymentCardNumber: String? = null,
    @ColumnInfo(name = "payment_user_id") var paymentCardUserId: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var paymentId: Int = 0
}