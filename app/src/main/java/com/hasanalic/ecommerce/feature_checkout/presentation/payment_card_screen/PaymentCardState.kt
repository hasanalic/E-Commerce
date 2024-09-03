package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID

data class PaymentCardState(
    val isLoading: Boolean = false,
    val doesUserHaveCards: Boolean = false,
    val canUserContinueToNextStep: Boolean = false,
    val cardId: String? = null,
    val userId: String = ANOMIM_USER_ID,
    val actionError: String? = null,
    val dataError: String? = null,
    val validationError: String? = null
)