package com.hasanalic.ecommerce.feature_product_detail.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity

@Dao
interface ReviewDao {

    @Query("SELECT * FROM Review WHERE review_product_id = :productId")
    suspend fun getReviewsByProductId(productId: String): List<ReviewEntity>?

    @Insert
    suspend fun insertAllReviews(vararg reviews: ReviewEntity): List<Long>
}