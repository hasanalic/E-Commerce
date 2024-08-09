package com.hasanalic.ecommerce.feature_product_detail.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity

interface ProductDetailRepository {
    suspend fun getReviewsByProductId(productId: String): Result<List<ReviewEntity>, DataError.Local>
}