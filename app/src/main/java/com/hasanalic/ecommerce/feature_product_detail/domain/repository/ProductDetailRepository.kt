package com.hasanalic.ecommerce.feature_product_detail.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_product_detail.domain.model.ProductDetail

interface ProductDetailRepository {
    suspend fun getProductDetailByUserIdAndProductId(userId: String, productId: String): Result<ProductDetail, DataError.Local>

    suspend fun getReviewsByProductId(productId: String): Result<List<ReviewEntity>, DataError.Local>
}