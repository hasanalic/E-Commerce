package com.hasanalic.ecommerce.feature_auth.domain.model

import com.hasanalic.ecommerce.core.domain.model.Error

enum class InputError: Error {
    EMPTY_NAME,
    EMPTY_EMAIL,
    EMPTY_PASSWORD
}