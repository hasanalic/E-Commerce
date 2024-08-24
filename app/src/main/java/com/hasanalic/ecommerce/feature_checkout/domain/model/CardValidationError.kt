package com.hasanalic.ecommerce.feature_checkout.domain.model

import com.hasanalic.ecommerce.core.domain.model.Error

enum class CardValidationError: Error {
    EMPTY_CARD_NAME,
    SHORT_CARD_NAME,
    EMPTY_CARD_NUMBER,
    INVALID_CARD_NUMBER,
    EMPTY_MONTH,
    INVALID_MONTH,
    EMPTY_YEAR,
    INVALID_YEAR,
    EMPTY_CVV,
    INVALID_CVV
}