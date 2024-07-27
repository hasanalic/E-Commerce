package com.hasanalic.ecommerce.feature_auth.domain.model

import com.hasanalic.ecommerce.core.domain.model.Error

enum class EmailValidationError: Error {
    INVALID_FORMAT
}