package com.hasanalic.ecommerce.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShoppingCartItems")
data class ShoppingCartItemsEntity(
    @ColumnInfo(name = "user_id") var userId: String? = null,
    @ColumnInfo(name = "product_id") var productId: String? = null,
    @ColumnInfo(name = "quantity") var quantity: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var shoppingCartEntityId: Int = 0
}