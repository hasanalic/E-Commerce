package com.hasanalic.ecommerce.core.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.repository.DatabaseInitializer
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.local.ReviewDao
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import javax.inject.Inject

class DatabaseInitializerImp @Inject constructor(
    private val productDao: ProductDao,
    private val reviewDao: ReviewDao
) : DatabaseInitializer {
    override suspend fun insertDefaultProducts(vararg productEntity: ProductEntity): Result<Unit, DataError.Local> {
        return try {
            val result = productDao.insertAllProductEntities(*productEntity)
            if (result.size == productEntity.size) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertDefaultReviews(vararg reviewEntity: ReviewEntity): Result<Unit, DataError.Local> {
        return try {
            val result = reviewDao.insertAllReviews(*reviewEntity)
            if (result.size == reviewEntity.size) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}