package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailValidationError

class UserEmailValidatorUseCase {
    operator fun invoke(email: String): Result<Unit, EmailValidationError> {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Result.Success(Unit)
        } else {
            Result.Error(EmailValidationError.INVALID_FORMAT)
        }
    }
}