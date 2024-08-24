package com.hasanalic.ecommerce.feature_checkout.presentation

data class CheckoutState(
    val userId: String? = null,
    val addressId: String? = null,
    val cargo: String? = null,
    val cardId: String? = null,
    val isLoading: Boolean = false,
    val isPaymentSuccessful: Boolean = false,
    val actionError: String? = null,
    val paymentError: String? = null
)