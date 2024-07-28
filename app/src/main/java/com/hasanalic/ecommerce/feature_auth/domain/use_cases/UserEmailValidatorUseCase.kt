package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailValidationError

class UserEmailValidatorUseCase {
    operator fun invoke(email: String): Result<Unit, EmailValidationError> {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return if (email.matches(Regex(emailPattern))) {
            Result.Success(Unit)
        } else {
            Result.Error(EmailValidationError.INVALID_FORMAT)
        }
    }
}