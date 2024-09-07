package com.hasanalic.ecommerce.feature_home.domain.use_case.filtered_products_use_cases

data class FilteredProductsUseCases (
    val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    val getProductsByFilterUseCase: GetProductsByFilterUseCase,
    val getProductsByKeywordUseCase: GetProductsByKeywordUseCase
)