package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.PasswordError
import com.hasanalic.ecommerce.core.domain.Result

class UserPasswordValidatorUseCase {

    operator fun invoke(password: String): Result<Unit, PasswordError> {
        if (password.length < 9) {
            return Result.Error(PasswordError.TOO_SHORT)
        }

        val hasUppercaseChar = password.any { it.isUpperCase() }
        if (!hasUppercaseChar) {
            return Result.Error(PasswordError.NO_UPPERCASE)
        }

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) {
            return Result.Error(PasswordError.NO_DIGIT)
        }

        return Result.Success(Unit)
    }
}