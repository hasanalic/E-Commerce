package com.hasanalic.ecommerce.feature_filter.domain.use_cases

data class FilterUseCases (
    val getBrandsUseCase: GetBrandsUseCase,
    val getBrandsByCategoryUseCase: GetBrandsByCategoryUseCase,
    val getCategoriesUseCase: GetCategoriesUseCase
)