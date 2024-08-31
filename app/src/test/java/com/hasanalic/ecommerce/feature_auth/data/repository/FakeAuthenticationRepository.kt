package com.hasanalic.ecommerce.feature_auth.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository

class FakeAuthenticationRepository: AuthenticationRepository {

    private val fakeUserId = 1L

    private val nonExistentEmail = "nonexistent@example.com"
    private val nonExistentPassword = "nonExistentPassword1"

    override suspend fun login(email: String, password: String): Result<Int, DataError.Local> {
        return if (email == nonExistentEmail || password == nonExistentPassword) {
            Result.Error(DataError.Local.NOT_FOUND)
        } else Result.Success(fakeUserId.toInt())
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Long, DataError.Local> {
        return Result.Success(fakeUserId)
    }
}