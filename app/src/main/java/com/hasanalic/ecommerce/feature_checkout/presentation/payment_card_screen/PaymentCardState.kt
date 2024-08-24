package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

data class PaymentCardState(
    val isLoading: Boolean = false,
    val doesUserHaveCards: Boolean = false,
    val canUserContinueToNextStep: Boolean = false,
    val cardId: String? = null,
    val actionError: String? = null,
    val dataError: String? = null,
    val validationError: String? = null
)