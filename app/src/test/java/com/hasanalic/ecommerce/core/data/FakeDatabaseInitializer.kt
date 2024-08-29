package com.hasanalic.ecommerce.core.data

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.repository.DatabaseInitializer
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity

class FakeDatabaseInitializer : DatabaseInitializer {
    val insertedProducts = mutableListOf<ProductEntity>()
    val insertedReviews = mutableListOf<ReviewEntity>()

    override suspend fun insertDefaultProducts(vararg productEntity: ProductEntity): Result<Unit, DataError.Local> {
        insertedProducts.addAll(productEntity)
        return Result.Success(Unit)
    }

    override suspend fun insertDefaultReviews(vararg reviewEntity: ReviewEntity): Result<Unit, DataError.Local> {
        insertedReviews.addAll(reviewEntity)
        return Result.Success(Unit)
    }
}