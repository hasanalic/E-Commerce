package com.hasanalic.ecommerce.feature_home.presentation.account_screen

import com.hasanalic.ecommerce.feature_home.domain.model.User

data class AccountState (
    val isLoading: Boolean = false,
    val user: User? = null,
    val isUserLoggedOut: Boolean = false,
    val shouldUserMoveToAuthActivity: Boolean = false,
    val isDeletionCompleted: Boolean = false,
    val dataError: String? = null,
    val actionError: String? = null
)