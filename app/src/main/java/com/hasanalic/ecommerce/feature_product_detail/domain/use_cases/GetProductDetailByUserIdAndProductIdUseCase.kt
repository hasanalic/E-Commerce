package com.hasanalic.ecommerce.feature_product_detail.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_product_detail.domain.model.ProductDetail
import com.hasanalic.ecommerce.feature_product_detail.domain.repository.ProductDetailRepository
import javax.inject.Inject

class GetProductDetailByUserIdAndProductIdUseCase @Inject constructor(
    private val productDetailRepository: ProductDetailRepository
) {
    suspend operator fun invoke(userId: String, productId: String): Result<ProductDetail, DataError.Local> {
        return productDetailRepository.getProductDetailByUserIdAndProductId(userId, productId)
    }
}