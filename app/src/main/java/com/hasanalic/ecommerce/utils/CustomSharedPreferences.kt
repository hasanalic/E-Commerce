package com.hasanalic.ecommerce.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.hasanalic.ecommerce.R

class CustomSharedPreferences {

    companion object {
        private const val PREFERENCE_SIGN_IN_TYPE = "sign_in_type"
        private const val PREFERENCE_DATABASE = "database"

        private var sharedPreferenceSignInWithSocialMedia: SharedPreferences? = null
        private var sharedPreferenceInitializeDatabase: SharedPreferences? = null

        @Volatile private var instance: CustomSharedPreferences? = null

        operator fun invoke(context: Context): CustomSharedPreferences = instance ?: synchronized(Any()) {
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferenceSignInWithSocialMedia = context.getSharedPreferences(PREFERENCE_SIGN_IN_TYPE,Context.MODE_PRIVATE)
            sharedPreferenceInitializeDatabase = context.getSharedPreferences(PREFERENCE_DATABASE,Context.MODE_PRIVATE)
            return CustomSharedPreferences()
        }
    }

    fun setSignInWithSocialMediaType(socialMediaType: String, context: Context) {
        sharedPreferenceSignInWithSocialMedia?.edit(commit = true) {
            putString(context.getString(R.string.sign_in_from),socialMediaType)
        }
    }
    fun getSignInWithSocialMediaType(context: Context): String? {
        return sharedPreferenceSignInWithSocialMedia?.getString(context.getString(R.string.sign_in_from),null)
    }

    // DATABASE
    fun setDatabaseInitialization(isInit: Boolean) {
        sharedPreferenceInitializeDatabase?.edit(commit = true) {
            putBoolean("isInit",isInit)
        }
    }
    fun getDatabaseInitialization() = sharedPreferenceInitializeDatabase?.getBoolean("isInit",true)

}