package com.hasanalic.ecommerce.feature_auth.data.local

import androidx.room.Dao
import androidx.room.Insert
import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: UserEntity): Long
}