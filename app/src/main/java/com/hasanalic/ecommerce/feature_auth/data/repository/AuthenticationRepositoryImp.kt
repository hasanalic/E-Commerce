package com.hasanalic.ecommerce.feature_auth.data.repository

import com.hasanalic.ecommerce.feature_auth.data.local.UserDao
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.data.mapper.toUser
import com.hasanalic.ecommerce.feature_auth.domain.model.User
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImp(
    private val userDao: UserDao
): AuthenticationRepository {
    override suspend fun login(email: String, password: String): Result<User, DataError.Local> {
        return try {
            val userEntity = userDao.getUserByUserNameAndPassword(email, password)

            userEntity?.let {
                Result.Success(it.toUser())
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
                Result.Error(DataError.Local.INSERTION_FAILD)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}