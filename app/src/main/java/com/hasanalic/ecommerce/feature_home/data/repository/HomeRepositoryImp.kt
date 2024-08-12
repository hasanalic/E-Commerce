package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_home.domain.repository.HomeRepository
import com.hasanalic.ecommerce.feature_filter.presentation.util.Filter
import com.hasanalic.ecommerce.feature_home.data.mapper.toProduct
import com.hasanalic.ecommerce.feature_home.domain.model.Brand
import com.hasanalic.ecommerce.feature_home.domain.model.Category
import com.hasanalic.ecommerce.feature_home.domain.model.Product
import com.hasanalic.ecommerce.utils.Resource
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

    /*
    override suspend fun getProductEntities(): Resource<List<ProductEntity>> {
        return try {
            val response = productDao.getProductEntities()
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"No Product Data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"error - getProducts")
        }
    }
     */

    /*
    override suspend fun getReviewsByProductId(productId: String): Resource<List<ReviewEntity>> {
        return try {
            val response = reviewDao.getReviewsByProductId(productId)
            response?.let {
                Resource.Success(it)
            }?: Resource.Error(null,"Failed to fetch reviews")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"getReviewsByProductId")
        }
    }

    override suspend fun insertAllReviews(vararg reviews: ReviewEntity): Resource<Boolean> {
        return try {
            val response = reviewDao.insertAllReviews(*reviews)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false, "Failed to insert reviews")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"insertAllReviews")
        }
    }
     */

    /*
    override suspend fun getFavoriteByProductId(
        userId: String,
        productId: String
    ): Resource<Boolean> {
        return try {
            val response = favoritesDao.getFavoriteByProductId(userId, productId)
            response?.let {
                Resource.Success(true)
            }?: Resource.Success(false)
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"error getFavoriteByProductId")
        }
    }

    override suspend fun getShoppingCartByProductId(
        userId: String,
        productId: String
    ): Resource<Boolean> {
        return try {
            val response = shoppingCartItemsDao.getShoppingCartEntityByProductId(userId, productId)
            response?.let {
                Resource.Success(true)
            }?: Resource.Success(false)
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"error getFavoriteByProductId")
        }
    }

     */
}