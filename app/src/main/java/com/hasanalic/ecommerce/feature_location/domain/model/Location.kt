package com.hasanalic.ecommerce.feature_location.domain.model

import com.hasanalic.ecommerce.feature_location.presentation.base.ListAdapterItem

data class Location (
    val addressUserId: String,
    var addressTitle: String,
    val addressDetail: String,
    val addressId: Int,
    override val id: Long = 0
) : ListAdapterItem