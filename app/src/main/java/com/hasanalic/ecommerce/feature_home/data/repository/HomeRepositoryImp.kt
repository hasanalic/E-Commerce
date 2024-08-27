package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.feature_home.data.mapper.toProduct
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import javax.inject.Inject
import kotlin.Exception

class HomeRepositoryImp @Inject constructor(
    private val productDao: ProductDao,
) : HomeRepository {
    override suspend fun getProductsByUserId(userId: String): Result<List<Product>, DataError.Local> {
        return try {
            val result = productDao.getProductsByUserId(userId)
            result?.let {
                Result.Success(result.map { productDao -> productDao.toProduct() })
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getProductEntityIdByBarcode(productBarcode: String): Result<Int, DataError.Local> {
        return try {
            val result = productDao.getProductEntityIdByBarcode(productBarcode)
            result?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}