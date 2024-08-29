package com.hasanalic.ecommerce.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CustomSharedPreferences {

    companion object {
        private const val PREFERENCE_DATABASE = "database"

        private var sharedPreferenceInitializeDatabase: SharedPreferences? = null

        @Volatile private var instance: CustomSharedPreferences? = null

        operator fun invoke(context: Context): CustomSharedPreferences = instance ?: synchronized(Any()) {
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferenceInitializeDatabase = context.getSharedPreferences(PREFERENCE_DATABASE,Context.MODE_PRIVATE)
            return CustomSharedPreferences()
        }
    }

    // DATABASE
    fun setDatabaseInitialization(isInit: Boolean) {
        sharedPreferenceInitializeDatabase?.edit(commit = true) {
            putBoolean("isInit",isInit)
        }
    }

    fun getDatabaseInitialization() = sharedPreferenceInitializeDatabase?.getBoolean("isInit",true)

}