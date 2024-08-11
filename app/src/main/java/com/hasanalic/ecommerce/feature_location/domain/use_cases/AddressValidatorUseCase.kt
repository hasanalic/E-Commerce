package com.hasanalic.ecommerce.feature_location.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_location.domain.model.AddressValidationError

class AddressValidatorUseCase {
    operator fun invoke(addressTitle: String, addressDetail: String) : Result<Unit, AddressValidationError> {
        if (addressTitle.isEmpty()) {
            return Result.Error(AddressValidationError.EMPTY_ADDRESS_TITLE)
        }

        if (addressDetail.isEmpty()) {
            return Result.Error(AddressValidationError.EMPTY_ADDRESS_DETAIL)
        }

        return Result.Success(Unit)
    }
}