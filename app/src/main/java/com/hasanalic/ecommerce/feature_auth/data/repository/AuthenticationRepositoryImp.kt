package com.hasanalic.ecommerce.feature_auth.data.repository

import com.hasanalic.ecommerce.feature_auth.data.local.UserDao
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImp(
    private val userDao: UserDao
): AuthenticationRepository {
    override suspend fun login(email: String, password: String): Result<Int, DataError.Local> {
        return try {
            val userEntity = userDao.getUserIdByEmailAndPassword(email, password)
            userEntity?.let {
                Result.Success(it)
            }?: Result.Error(DataError.Local.NOT_FOUND)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Long, DataError.Local> {
        return try {
            val userEntity = UserEntity(userName = name, userEmail = email, userPassword = password)
            val insertedUserId = userDao.insertUser(userEntity)

            if (insertedUserId > 0) {
                Result.Success(insertedUserId)
            } else {
                Result.Error(DataError.Local.INSERTION_FAILED)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}