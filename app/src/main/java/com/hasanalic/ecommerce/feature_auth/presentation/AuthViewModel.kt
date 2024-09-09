package com.hasanalic.ecommerce.feature_auth.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.database_initialization.DatabaseInitializerUseCases
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sharedPreferencesUseCases: SharedPreferencesUseCases,
    private val databaseInitializerUseCases: DatabaseInitializerUseCases
): ViewModel() {

    private var _authState = MutableLiveData(AuthState())
    val authState: LiveData<AuthState> = _authState

    init {
        checkIfUserAlreadyLoggedIn()
        checkIsDatabaseInitialized()
    }

    private fun checkIfUserAlreadyLoggedIn() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        if (userId != null) {
            _authState.value = _authState.value!!.copy(
                isUserAlreadyLoggedIn = true
            )
        }
    }

    private fun checkIsDatabaseInitialized() {
        _authState.value = _authState.value!!.copy(isLoading = true)
        viewModelScope.launch {
            val isDatabaseInitialized = sharedPreferencesUseCases.isDatabaseInitializedUseCase()
            if (isDatabaseInitialized) {
                _authState.value = _authState.value!!.copy(isDatabaseInitialized = true, isLoading = false)
            } else {
                insertProductAndReviewEntities()
            }
        }
    }

    private suspend fun insertProductAndReviewEntities() {
        when(val result = databaseInitializerUseCases.insertDefaultProductsUseCase()) {
            is Result.Error -> handleInsertDatabaseError(result.error)
            is Result.Success -> insertReviewEntities()
        }
    }

    private suspend fun insertReviewEntities() {
        when(val result = databaseInitializerUseCases.insertDefaultReviewsUseCase()) {
            is Result.Error -> handleInsertDatabaseError(result.error)
            is Result.Success -> {
                setDatabaseInitializedAsTrue()
                _authState.value = _authState.value!!.copy(isDatabaseInitialized = true, isLoading = false)
            }
        }
    }

    private fun handleInsertDatabaseError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.INSERTION_FAILED -> "Saving to database failed."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _authState.value = _authState.value!!.copy(
            isLoading = false,
            dataError = message
        )
    }

    private fun setDatabaseInitializedAsTrue() {
        sharedPreferencesUseCases.setDatabaseInitializedUseCase(true)
    }
}