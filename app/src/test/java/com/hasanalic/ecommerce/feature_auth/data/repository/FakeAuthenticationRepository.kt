package com.hasanalic.ecommerce.feature_auth.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.User
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository

class FakeAuthenticationRepository: AuthenticationRepository {

    private val fakeUser = User(1, "John Doe",  "john.doe@example.com")
    private val fakeUserId = 1L

    override suspend fun login(email: String, password: String): Result<User, DataError.Local> {
        return Result.Success(fakeUser)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Long, DataError.Local> {
        return Result.Success(fakeUserId)
    }
}