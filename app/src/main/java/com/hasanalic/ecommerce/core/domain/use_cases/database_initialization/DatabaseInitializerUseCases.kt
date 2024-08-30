package com.hasanalic.ecommerce.core.domain.use_cases.database_initialization

data class DatabaseInitializerUseCases (
    val insertDefaultProductsUseCase: InsertDefaultProductsUseCase,
    val insertDefaultReviewsUseCase: InsertDefaultReviewsUseCase
)