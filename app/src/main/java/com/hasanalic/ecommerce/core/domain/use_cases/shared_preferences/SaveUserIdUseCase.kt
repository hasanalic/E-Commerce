package com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences

import com.hasanalic.ecommerce.core.domain.repository.SharedPreferencesDataSource
import javax.inject.Inject

class SaveUserIdUseCase @Inject constructor(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
) {
    operator fun invoke(userId: String) {
        sharedPreferencesDataSource.saveUserId(userId)
    }
}