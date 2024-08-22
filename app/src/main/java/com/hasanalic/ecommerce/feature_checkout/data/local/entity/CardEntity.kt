package com.hasanalic.ecommerce.feature_checkout.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Card")
data class CardEntity(
    @ColumnInfo(name = "card_name") var cardName: String? = null,
    @ColumnInfo(name = "card_number") var cardNumber: String? = null,
    @ColumnInfo(name = "card_user_id") var cardUserId: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var cardId: Int = 0
}