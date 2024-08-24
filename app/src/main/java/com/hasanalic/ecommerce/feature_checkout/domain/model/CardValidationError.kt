package com.hasanalic.ecommerce.feature_checkout.domain.model

import com.hasanalic.ecommerce.core.domain.model.Error

enum class CardValidationError: Error {
    EMPTY_CARD_NAME,
    SHORT_CARD_NAME,
    EMPTY_CARD_NUMBER,
    SHORT_CARD_NUMBER,
    EMPTY_MONTH,
    SHORT_MONTH,
    EMPTY_YEAR,
    SHORT_YEAR,
    EMPTY_CVV,
    SHORT_CVV
}