package com.hasanalic.ecommerce.feature_checkout.domain.model

data class Address(
    var addressId: String,
    var addressUserId: String,
    var addressTitle: String,
    var addressDetail: String,
    var isSelected: Boolean = false
)
