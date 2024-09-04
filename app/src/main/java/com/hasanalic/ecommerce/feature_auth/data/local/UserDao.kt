package com.hasanalic.ecommerce.feature_auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT userId FROM User WHERE user_email = :email AND user_password = :password")
    suspend fun getUserIdByEmailAndPassword(email: String, password: String): Int?

    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUser(userId: String): UserEntity?

    @Query("DELETE FROM User WHERE userId = :userId")
    suspend fun deleteUser(userId: String): Int
}