package com.hasanalic.ecommerce.feature_location.data.repository

import com.hasanalic.ecommerce.core.domain.model.Address
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository

class FakeAddressRepository : AddressRepository {

    private val addressList = mutableListOf(Address("1","1","title","detail",false))
    private val addressEntityList = mutableListOf(AddressEntity("1","title","detail"))

    override suspend fun getAddressListByUserId(userId: String): Result<List<Address>, DataError.Local> {
        return Result.Success(addressList)
    }

    override suspend fun getAddressEntityListByUserId(userId: String): Result<List<AddressEntity>, DataError.Local> {
        return Result.Success(addressEntityList)
    }

    override suspend fun insertAddressEntity(addressEntity: AddressEntity): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }

    override suspend fun deleteUserAddress(
        userId: String,
        addressId: String
    ): Result<Unit, DataError.Local> {
        return if (userId == addressEntityList[0].addressUserId) Result.Success(Unit) else Result.Error(DataError.Local.DELETION_FAILED)
    }

    override suspend fun getAddressEntityByUserIdAndAddressId(
        userId: String,
        addressId: String
    ): Result<AddressEntity, DataError.Local> {
        return Result.Success(addressEntityList.first())
    }
}