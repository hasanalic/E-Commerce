package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailError

class UserEmailValidatorUseCase {
    operator fun invoke(email: String): Result<Unit, EmailError> {
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Result.Success(Unit)
        } else {
            Result.Error(EmailError.INVALID_FORMAT)
        }
    }
}