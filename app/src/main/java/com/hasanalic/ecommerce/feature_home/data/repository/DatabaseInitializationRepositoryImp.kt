package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_home.domain.repository.DatabaseInitializationRepository
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import javax.inject.Inject

class DatabaseInitializationRepositoryImp @Inject constructor(
    private val productDao: ProductDao
) : DatabaseInitializationRepository {
    override suspend fun insertAllProductEntities(vararg products: ProductEntity): Result<Unit, DataError.Local> {
        return try {
            val result = productDao.insertAllProductEntities(*products)
            if (result.isNotEmpty()) Result.Success(Unit)
            else Result.Error(DataError.Local.INSERTION_FAILED)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertAllReviews(vararg reviews: ReviewEntity): Result<Unit, DataError.Local> {
        TODO("Not yet implemented")
    }
}