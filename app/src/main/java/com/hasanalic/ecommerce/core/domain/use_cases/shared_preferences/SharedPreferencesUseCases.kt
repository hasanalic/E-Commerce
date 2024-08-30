package com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences

data class SharedPreferencesUseCases (
    val getUserIdUseCase: GetUserIdUseCase,
    val isDatabaseInitializedUseCase: IsDatabaseInitializedUseCase,
    val saveUserIdUseCase: SaveUserIdUseCase,
    val setDatabaseInitializedUseCase: SetDatabaseInitializedUseCase,
    val logOutUserUseCase: LogOutUserUseCase
)