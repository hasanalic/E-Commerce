package com.hasanalic.ecommerce.feature_home.domain.use_case.home_use_cases

data class HomeUseCases(
    val getProductsByUserIdUseCase: GetProductsByUserIdUseCase,
    val getCategoriesUseCase: GetCategoriesUseCase,
    val getBrandsUseCase: GetBrandsUseCase,
    val getBrandsByCategoryUseCase: GetBrandsByCategoryUseCase,
    val getProductEntityIdByBarcodeUseCase: GetProductEntityIdByBarcodeUseCase
)