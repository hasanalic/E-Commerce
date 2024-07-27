package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.InputValidationError

class UserInputValidatorUseCase {

    operator fun invoke(name: String? = null, email: String, password: String): Result<Unit, InputValidationError> {
        name?.let {
            if (name.isBlank()) {
                return Result.Error(InputValidationError.EMPTY_NAME)
            }
        }

        if (email.isBlank()) {
            return Result.Error(InputValidationError.EMPTY_EMAIL)
        }

        if (password.isBlank()) {
            return Result.Error(InputValidationError.EMPTY_PASSWORD)
        }

        return Result.Success(Unit)
    }
}