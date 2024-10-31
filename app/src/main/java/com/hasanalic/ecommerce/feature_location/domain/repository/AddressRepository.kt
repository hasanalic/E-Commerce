package com.hasanalic.ecommerce.feature_location.domain.repository

import com.hasanalic.ecommerce.feature_checkout.domain.model.Address
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.domain.model.Location

interface AddressRepository {
    suspend fun getAddressListByUserId(userId: String): Result<List<Address>, DataError.Local>

    suspend fun getAddressEntityListByUserId(userId: String): Result<List<Location>, DataError.Local>

    suspend fun insertAddressEntity(addressEntity: AddressEntity): Result<Unit, DataError.Local>

    suspend fun deleteUserAddress(userId: String, addressId: String): Result<Unit, DataError.Local>

    suspend fun getAddressEntityByUserIdAndAddressId(userId: String, addressId: String): Result<AddressEntity, DataError.Local>
}