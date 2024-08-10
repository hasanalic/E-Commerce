package com.hasanalic.ecommerce.feature_product_detail.domain.use_cases

data class ProductDetailUseCases(
    val getProductDetailByUserIdAndProductIdUseCase: GetProductDetailByUserIdAndProductIdUseCase,
    val getReviewsByProductIdUseCase: GetReviewsByProductIdUseCase
)