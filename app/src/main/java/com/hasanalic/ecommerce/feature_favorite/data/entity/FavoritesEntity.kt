package com.hasanalic.ecommerce.feature_favorite.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorites")
data class FavoritesEntity(
    @ColumnInfo(name = "user_id") var userId: String? = null,
    @ColumnInfo(name = "product_id") var productId: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var favoriteEntityId: Int = 0
}