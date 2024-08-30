package com.hasanalic.ecommerce.feature_auth.presentation

data class AuthState (
    val isLoading: Boolean = false,
    val isUserAlreadyLoggedIn: Boolean = false,
    val isDatabaseInitialized: Boolean = false,
    val dataError: String? = null
)