package com.hasanalic.ecommerce.feature_home.domain.use_case.user_use_cases

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.User
import com.hasanalic.ecommerce.feature_home.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User, DataError.Local> {
        return userRepository.getUser(userId)
    }
}