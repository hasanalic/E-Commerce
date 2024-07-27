package com.hasanalic.ecommerce.feature_auth.data.mapper

import com.hasanalic.ecommerce.feature_auth.data.local.entities.UserEntity
import com.hasanalic.ecommerce.feature_auth.domain.model.User

fun UserEntity.toUser(): User {
    return User(userId = this.userId, userName = this.userName, userEmail = this.userEmail)
}