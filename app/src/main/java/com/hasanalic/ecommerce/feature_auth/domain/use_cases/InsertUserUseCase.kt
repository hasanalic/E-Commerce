package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.DataError
import com.hasanalic.ecommerce.core.domain.Result
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Long, DataError> {
        return authenticationRepository.register(name, email, password)
    }
}