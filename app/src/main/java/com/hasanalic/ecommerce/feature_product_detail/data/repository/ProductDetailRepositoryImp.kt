package com.hasanalic.ecommerce.feature_product_detail.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.data.local.ProductDao
import com.hasanalic.ecommerce.feature_product_detail.data.local.ReviewDao
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_product_detail.data.mapper.toProductDetail
import com.hasanalic.ecommerce.feature_product_detail.domain.model.ProductDetail
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository
import javax.inject.Inject

class ProductDetailRepositoryImp @Inject constructor(
    private val productDao: ProductDao,
    private val reviewDao: ReviewDao
): ProductDetailRepository{
    override suspend fun getProductDetailByUserIdAndProductId(
        userId: String,
        productId: String
    ): Result<ProductDetail, DataError.Local> {
        return try {
            val result = productDao.getProductByUserIdAndProductId(userId, productId)
            result?.let {
                Result.Success(it.toProductDetail())
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getReviewsByProductId(productId: String): Result<List<ReviewEntity>, DataError.Local> {
        return try {
            val result = reviewDao.getReviewsByProductId(productId)
            result?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}