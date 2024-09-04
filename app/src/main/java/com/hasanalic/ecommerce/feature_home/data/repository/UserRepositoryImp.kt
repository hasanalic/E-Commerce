package com.hasanalic.ecommerce.feature_home.data.repository

import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.data.local.UserDao
import com.hasanalic.ecommerce.feature_home.data.mapper.toUser
import com.hasanalic.ecommerce.feature_home.domain.model.User
import com.hasanalic.ecommerce.feature_home.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val userDao: UserDao
): UserRepository {
    override suspend fun getUser(userId: String): Result<User, DataError.Local> {
        return try {
            val result = userDao.getUser(userId)
            result?.let {
                Result.Success(it.toUser())
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Unit, DataError.Local> {
        return try {
            val result = userDao.deleteUser(userId)
            if (result > 0) {
                Result.Success(Unit)
            } else {
                Result.Error(DataError.Local.DELETION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}