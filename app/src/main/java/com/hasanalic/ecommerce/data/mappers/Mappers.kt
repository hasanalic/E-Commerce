package com.hasanalic.ecommerce.data.mappers

import com.hasanalic.ecommerce.data.dto.AddressEntity
import com.hasanalic.ecommerce.domain.model.Address

fun AddressEntity.toAddress() = Address(
    addressId.toString(), addressUserId!!, addressTitle!!, addressDetail!!
)