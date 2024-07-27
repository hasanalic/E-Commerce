package com.hasanalic.ecommerce.feature_auth.domain.use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.User
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository
import javax.inject.Inject

class GetUserByEmailAndPassUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User, DataError.Local> {
        return authenticationRepository.login(email,password)
    }
}