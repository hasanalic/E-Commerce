package com.hasanalic.ecommerce.feature_product_detail.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Review")
data class ReviewEntity(
    @ColumnInfo(name = "review_product_id") var reviewProductId: String,
    @ColumnInfo(name = "review_name") var reviewName: String,
    @ColumnInfo(name = "review_profile_photo") var reviewProfilePhoto: String,
    @ColumnInfo(name = "review_date") var reviewDate: String,
    @ColumnInfo(name = "review_title") var reviewTitle: String,
    @ColumnInfo(name = "review_content") var reviewContent: String,
    @ColumnInfo(name = "review_product_photo") var reviewProductPhoto: String,
    @ColumnInfo(name = "review_star") var reviewStar: Int
) {
    @PrimaryKey(autoGenerate = true)
    var reviewId: Int = 0
}