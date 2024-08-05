package com.hasanalic.ecommerce.feature_home.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ShoppingCartItems")
data class ShoppingCartItemsEntity(
    @ColumnInfo(name = "user_id") var userId: String,
    @ColumnInfo(name = "product_id") var productId: String,
    @ColumnInfo(name = "quantity") var quantity: Int
) {
    @PrimaryKey(autoGenerate = true)
    var shoppingCartEntityId: Int = 0
}