package com.hasanalic.ecommerce.feature_auth.domain.use_cases

data class AuthUseCases(
    val insertUserUseCase: InsertUserUseCase,
    val userPasswordValidatorUseCase: UserPasswordValidatorUseCase
)
