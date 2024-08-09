package com.hasanalic.ecommerce.feature_home.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity

interface DatabaseInitializationRepository {
    suspend fun insertAllProductEntities(vararg products: ProductEntity): Result<Unit, DataError.Local>

    suspend fun insertAllReviews(vararg reviews: ReviewEntity): Result<Unit, DataError.Local>
}