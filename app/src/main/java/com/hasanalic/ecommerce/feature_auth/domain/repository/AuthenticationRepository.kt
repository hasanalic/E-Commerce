package com.hasanalic.ecommerce.feature_auth.domain.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result

interface AuthenticationRepository {

    suspend fun login(email: String, password: String)

    suspend fun register(name: String, email: String, password: String): Result<Long, DataError.Local>
}