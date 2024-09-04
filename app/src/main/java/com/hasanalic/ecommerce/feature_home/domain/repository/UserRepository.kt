package com.hasanalic.ecommerce.feature_home.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_home.domain.model.User

interface UserRepository {

    suspend fun getUser(userId: String): Result<User, DataError.Local>

    suspend fun deleteUser(userId: String): Result<Unit, DataError.Local>
}