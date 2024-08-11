package com.hasanalic.ecommerce.feature_location.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.data.local.entity.AddressEntity
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import javax.inject.Inject

class GetAddressEntityByUserIdAndAddressIdUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(userId: String, addressId: String): Result<AddressEntity, DataError.Local> {
        return addressRepository.getAddressEntityByUserIdAndAddressId(userId, addressId)
    }
}