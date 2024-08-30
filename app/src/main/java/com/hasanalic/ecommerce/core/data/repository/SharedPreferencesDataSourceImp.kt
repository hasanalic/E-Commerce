package com.hasanalic.ecommerce.core.data.repository

import android.content.SharedPreferences
import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import com.hasanalic.ecommerce.di.DbPrefs
import com.hasanalic.ecommerce.di.UserPrefs
import javax.inject.Inject

class SharedPreferencesDataSourceImp @Inject constructor(
    @UserPrefs private val userSharedPreferences: SharedPreferences,
    @DbPrefs private val databaseSharedPreferences: SharedPreferences
) : SharedPreferencesDataSource {
    override fun saveUserId(userId: String) {
        userSharedPreferences.edit().putString("USER_ID", userId).apply()
    }

    override fun getUserId(): String? {
        return userSharedPreferences.getString("USER_ID", null)
    }

    override fun setUserNull() {
        userSharedPreferences.edit().putString("USER_ID", null).apply()
    }

    override fun setDatabaseInitialized(isInitialized: Boolean) {
        databaseSharedPreferences.edit().putBoolean("DB_INITIALIZED", isInitialized).apply()
    }

    override fun isDatabaseInitialized(): Boolean {
        return databaseSharedPreferences.getBoolean("DB_INITIALIZED", false)
    }
}