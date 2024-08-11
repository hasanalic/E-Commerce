package com.hasanalic.ecommerce.feature_location.presentation

import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity

data class LocationState (
    val isLoading: Boolean = false,
    val addressEntityList: MutableList<AddressEntity> = mutableListOf(),
    val isAddressInsertionSuccessful: Boolean = false,
    val isAddressDeletionSuccessful: Boolean = false,
    val validationError: String? = null,
    val dataError: String? = null,
    val actionError: String? = null
)