package com.hasanalic.ecommerce.core.domain.repository

interface SharedPreferencesDataSource {
    fun saveUserId(userId: String)
    fun getUserId(): String?
    fun setUserNull()

    fun setDatabaseInitialized(isInitialized: Boolean)
    fun isDatabaseInitialized(): Boolean
}