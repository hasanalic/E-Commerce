package com.hasanalic.ecommerce.feature_location.data.mapper

import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_checkout.domain.model.Address
import com.hasanalic.ecommerce.feature_location.domain.model.Location

fun AddressEntity.toAddress() = Address(
    addressId.toString(), addressUserId, addressTitle, addressDetail
)

fun AddressEntity.toLocation() = Location(
    addressUserId = this.addressUserId,
    addressTitle = this.addressTitle,
    addressDetail = this.addressDetail,
    addressId = this.addressId
)