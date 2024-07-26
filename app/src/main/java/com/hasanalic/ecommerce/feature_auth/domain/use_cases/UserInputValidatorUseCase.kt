package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.InputError

class UserInputValidatorUseCase {

    operator fun invoke(name: String, email: String, password: String): Result<Unit, InputError> {
        if (name.isBlank()) {
            return Result.Error(InputError.EMPTY_NAME)
        }

        if (email.isBlank()) {
            return Result.Error(InputError.EMPTY_EMAIL)
        }

        if (password.isBlank()) {
            return Result.Error(InputError.EMPTY_PASSWORD)
        }

        return Result.Success(Unit)
    }
}