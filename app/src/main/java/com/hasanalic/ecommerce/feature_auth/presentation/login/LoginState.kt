package com.hasanalic.ecommerce.feature_auth.presentation.login

data class LoginState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val dataError: String? = null,
    val validationError: String? = null
)