package com.hasanalic.ecommerce.feature_location.data.mapper

import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_checkout.domain.model.Address

fun AddressEntity.toAddress() = Address(
    addressId.toString(), addressUserId, addressTitle, addressDetail
)