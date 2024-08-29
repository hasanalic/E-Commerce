package com.hasanalic.ecommerce.core.domain.use_cases.database_initilization

data class DatabaseInitializerUseCases (
    val insertDefaultProductsUseCase: InsertDefaultProductsUseCase,
    val insertDefaultReviewsUseCase: InsertDefaultReviewsUseCase
)