package com.hasanalic.ecommerce.feature_location.domain.model

import com.hasanalic.ecommerce.core.domain.model.Error

enum class AddressValidationError : Error {
    EMPTY_ADDRESS_TITLE,
    EMPTY_ADDRESS_DETAIL
}