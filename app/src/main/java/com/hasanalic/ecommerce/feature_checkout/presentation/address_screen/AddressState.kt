package com.hasanalic.ecommerce.feature_checkout.presentation.address_screen

import com.hasanalic.ecommerce.feature_checkout.domain.model.Address

data class AddressState(
    val isLoading: Boolean = false,
    val addressList: List<Address> = listOf(),
    val dataError: String? = null
)