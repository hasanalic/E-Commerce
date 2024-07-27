package com.hasanalic.ecommerce.feature_auth.domain.use_cases

data class AuthUseCases(
    val insertUserUseCase: InsertUserUseCase,
    val getUserByEmailAndPassUseCase: GetUserByEmailAndPassUseCase,
    val userEmailValidatorUseCase: UserEmailValidatorUseCase,
    val userInputValidatorUseCase: UserInputValidatorUseCase,
    val userPasswordValidatorUseCase: UserPasswordValidatorUseCase
)
