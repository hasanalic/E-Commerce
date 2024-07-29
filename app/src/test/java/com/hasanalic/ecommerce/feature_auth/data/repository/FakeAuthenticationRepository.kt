package com.hasanalic.ecommerce.feature_auth.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.User
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository

class FakeAuthenticationRepository: AuthenticationRepository {

    private val validName = "John Doe"
    private val validEmail = "john.doe@example.com"
    private val fakeUserId = 1L
    private val fakeUser = User(fakeUserId.toInt(), validName,  validEmail)

    private val nonExistentEmail = "nonexistent@example.com"
    private val nonExistentPassword = "nonExistentPassword1"

    override suspend fun login(email: String, password: String): Result<User, DataError.Local> {
        return if (email == nonExistentEmail || password == nonExistentPassword) {
            Result.Error(DataError.Local.NOT_FOUND)
        } else Result.Success(fakeUser)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Long, DataError.Local> {
        return Result.Success(fakeUserId)
    }
}