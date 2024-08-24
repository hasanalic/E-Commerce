package com.hasanalic.ecommerce.feature_checkout.domain.use_cases

data class CardUseCases(
    val getCardByUserIdAndCardIdUseCase: GetCardByUserIdAndCardIdUseCase,
    val getCardsByUserIdUseCase: GetCardsByUserIdUseCase,
    val insertCardEntityUseCase: InsertCardEntityUseCase,
    val cardValidatorUseCase: CardValidatorUseCase
)