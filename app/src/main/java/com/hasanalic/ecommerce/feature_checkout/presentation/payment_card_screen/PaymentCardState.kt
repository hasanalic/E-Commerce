package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

data class PaymentCardState(
    val isLoading: Boolean = false,
    val isPaymentSuccessful: Boolean = false,
    val doesUserHaveCards: Boolean = false,
    val actionError: String? = null
)