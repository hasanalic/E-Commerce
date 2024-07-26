package com.hasanalic.ecommerce.feature_auth.domain.model

import com.hasanalic.ecommerce.core.domain.model.Error

enum class PasswordError: Error {
    TOO_SHORT,
    NO_UPPERCASE,
    NO_DIGIT
}