package com.hasanalic.ecommerce.feature_auth.presentation.register

data class RegisterState(
    val isLoading: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val dataError: String? = null,
    val validationError: String? = null
)



/*
 *** EXAMPLE OF UI STATE WITH SEALED CLASS ***

    sealed class SearchUiState {
        data object Loading : SearchUiState()
        data object Idle : SearchUiState()
        data class Success(val photos: List<FlickrPhoto>) : SearchUiState()
        data object EmptyResult : SearchUiState()
        data class Error(val exception: Throwable) : SearchUiState()
    }

 */