package com.hasanalic.ecommerce.core.data

import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource

class FakeSharedPreferencesDataSourceImp : SharedPreferencesDataSource {

    var fakeUserId: String? = null
    var isInitialized: Boolean = false

    override fun saveUserId(userId: String) {
        fakeUserId = userId
    }

    override fun getUserId(): String? {
        return fakeUserId
    }

    override fun setUserNull() {
        fakeUserId = null
    }

    override fun setDatabaseInitialized(isInitialized: Boolean) {
        this.isInitialized = isInitialized
    }

    override fun isDatabaseInitialized(): Boolean {
        return isInitialized
    }
}