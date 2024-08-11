package com.hasanalic.ecommerce.feature_location.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import javax.inject.Inject

class InsertAddressEntityUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(addressEntity: AddressEntity): Result<Unit, DataError.Local> {
        return addressRepository.insertAddressEntity(addressEntity)
    }
}