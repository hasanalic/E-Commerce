package com.hasanalic.ecommerce.feature_product_detail.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_product_detail.domain.model.ProductDetail
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository

class FakeProductDetailRepository : ProductDetailRepository {
    private val reviewList = listOf(ReviewEntity("1", "name", "pp",
        "date", "title", "content", "pp", 1))

    private val productDetail = ProductDetail("1","category",
        "photo","brand",
        "detail",1,
        1,1.0,
        "1","shipping", "store","",false,false)

    override suspend fun getProductDetailByUserIdAndProductId(
        userId: String,
        productId: String
    ): Result<ProductDetail, DataError.Local> {
        return if (productId == productDetail.productId) Result.Success(productDetail)
        else Result.Error(DataError.Local.NOT_FOUND)
    }

    override suspend fun getReviewsByProductId(productId: String): Result<List<ReviewEntity>, DataError.Local> {
        return Result.Success(reviewList)
    }
}