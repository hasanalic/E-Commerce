package com.hasanalic.ecommerce.feature_auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE user_email = :email AND user_password = :password")
    suspend fun getUserByUserNameAndPassword(email: String, password: String): UserEntity?

    @Insert
    suspend fun insertUser(user: UserEntity): Long
}