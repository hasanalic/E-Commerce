package com.hasanalic.ecommerce.feature_location.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.domain.model.Location
import com.hasanalic.ecommerce.feature_location.domain.repository.AddressRepository
import javax.inject.Inject

class GetLocationListByUserIdUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Location>, DataError.Local> {
        return addressRepository.getAddressEntityListByUserId(userId)
    }
}