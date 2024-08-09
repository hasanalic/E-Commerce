package com.hasanalic.ecommerce.feature_product_detail.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository

class ProductDetailRepositoryImp : ProductDetailRepository{
    override suspend fun getReviewsByProductId(productId: String): Result<List<ReviewEntity>, DataError.Local> {
        TODO("Not yet implemented")
    }
}