package com.hasanalic.ecommerce.core.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity

interface DatabaseInitializer {

    suspend fun insertDefaultProducts(vararg productEntity: ProductEntity): Result<Unit, DataError.Local>

    suspend fun insertDefaultReviews(vararg reviewEntity: ReviewEntity): Result<Unit, DataError.Local>
}