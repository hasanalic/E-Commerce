package com.hasanalic.ecommerce.feature_product_detail.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository
import javax.inject.Inject

class GetReviewsByProductIdUseCase @Inject constructor(
    private val productDetailRepository: ProductDetailRepository
) {
    suspend operator fun invoke(productId: String): Result<List<ReviewEntity>, DataError.Local> {
        return productDetailRepository.getReviewsByProductId(productId)
    }
}