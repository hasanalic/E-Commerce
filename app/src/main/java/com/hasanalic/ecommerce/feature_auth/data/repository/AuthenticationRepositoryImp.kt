package com.hasanalic.ecommerce.feature_auth.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteFullException
import com.hasanalic.ecommerce.feature_auth.data.local.UserDao
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import com.hasanalic.ecommerce.core.domain.DataError
import com.hasanalic.ecommerce.core.domain.Result
import com.hasanalic.ecommerce.feature_auth.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImp(
    private val userDao: UserDao
): AuthenticationRepository {
    override suspend fun login(email: String, password: String) {
        TODO("Not yet implemented")
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
        } catch (e: SQLiteConstraintException) {
            Result.Error(DataError.Local.CONSTRAINT_VIOLATION)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: SQLiteDatabaseCorruptException) {
            Result.Error(DataError.Local.DB_CORRUPTION)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}