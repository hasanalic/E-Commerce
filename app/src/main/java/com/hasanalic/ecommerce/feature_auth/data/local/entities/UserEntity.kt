package com.hasanalic.ecommerce.feature_auth.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity(
    @ColumnInfo(name = "user_name") var userName: String? = null,
    @ColumnInfo(name = "user_email") var userEmail: String? = null,
    @ColumnInfo(name = "user_password") var userPassword: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0
}