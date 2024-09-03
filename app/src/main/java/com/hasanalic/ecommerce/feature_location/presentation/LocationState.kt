package com.hasanalic.ecommerce.feature_location.presentation

import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity

data class LocationState (
    val isLoading: Boolean = false,
    val addressEntityList: MutableList<AddressEntity> = mutableListOf(),
    val userId: String = ANOMIM_USER_ID,
    val isAddressInsertionSuccessful: Boolean = false,
    val isAddressDeletionSuccessful: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val validationError: String? = null,
    val dataError: String? = null,
    val actionError: String? = null
)