package com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences

import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import javax.inject.Inject

class SetDatabaseInitializedUseCase @Inject constructor(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    operator fun invoke(isInitialized: Boolean) {
        sharedPreferencesDataSource.setDatabaseInitialized(isInitialized)
    }
}