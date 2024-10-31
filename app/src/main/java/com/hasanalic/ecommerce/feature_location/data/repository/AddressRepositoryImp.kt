package com.hasanalic.ecommerce.feature_location.data.repository

import com.hasanalic.ecommerce.feature_checkout.domain.model.Address
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.data.local.AddressDao
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.data.mapper.toAddress
import com.hasanalic.ecommerce.feature_location.data.mapper.toLocation
import com.hasanalic.ecommerce.feature_location.domain.model.Location
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImp @Inject constructor(
    private val addressDao: AddressDao
) : AddressRepository {
    override suspend fun getAddressListByUserId(userId: String): Result<List<Address>, DataError.Local> {
        return try {
            val result = addressDao.getAddressEntityListByUserId(userId)
            result?.let {
                Result.Success(it.map { addressEntity -> addressEntity.toAddress() })
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getAddressEntityListByUserId(userId: String): Result<List<Location>, DataError.Local> {
        return try {
            val result = addressDao.getAddressEntityListByUserId(userId)
            result?.let {
                Result.Success(it.map { addressEntity -> addressEntity.toLocation() })
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun insertAddressEntity(addressEntity: AddressEntity): Result<Unit, DataError.Local> {
        return try {
            val result = addressDao.insertAddressEntity(addressEntity)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteUserAddress(
        userId: String,
        addressId: String
    ): Result<Unit, DataError.Local> {
        return try {
            val result = addressDao.deleteAddress(userId, addressId)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.DELETION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun getAddressEntityByUserIdAndAddressId(
        userId: String,
        addressId: String
    ): Result<AddressEntity, DataError.Local> {
        return try {
            val result = addressDao.getAddressEntityByUserIdAndAddressId(userId, addressId)
            result?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}