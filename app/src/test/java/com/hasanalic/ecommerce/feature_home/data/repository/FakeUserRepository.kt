package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.User
import com.hasanalic.ecommerce.feature_home.domain.repository.UserRepository

class FakeUserRepository : UserRepository {
    override suspend fun getUser(userId: String): Result<User, DataError.Local> {
        return Result.Success(User("1","name","user@example.com"))
    }

    override suspend fun deleteUser(userId: String): Result<Unit, DataError.Local> {
        return Result.Success(Unit)
    }
}