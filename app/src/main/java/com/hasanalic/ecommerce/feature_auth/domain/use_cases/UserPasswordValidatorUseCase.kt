package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.feature_auth.domain.model.PasswordValidationError
import com.hasanalic.ecommerce.core.domain.model.Result

class UserPasswordValidatorUseCase {

    operator fun invoke(password: String): Result<Unit, PasswordValidationError> {
        if (password.length < 8) {
            return Result.Error(PasswordValidationError.TOO_SHORT)
        }

        val hasUppercaseChar = password.any { it.isUpperCase() }
        if (!hasUppercaseChar) {
            return Result.Error(PasswordValidationError.NO_UPPERCASE)
        }

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) {
            return Result.Error(PasswordValidationError.NO_DIGIT)
        }

        return Result.Success(Unit)
    }
}