package com.hasanalic.ecommerce.feature_home.presentation.account_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_home.domain.use_case.user_use_cases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val sharedPreferencesUseCases: SharedPreferencesUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private var _accountState = MutableLiveData(AccountState())
    val accountState: LiveData<AccountState> = _accountState

    fun getUserIfLoggedIn() {
        val userId = sharedPreferencesUseCases.getUserIdUseCase()
        if (userId == null) {
            _accountState.value = AccountState(
                shouldUserMoveToAuthActivity = true
            )
            return
        }

        getUser(userId)
    }

    private fun getUser(userId: String) {
        _accountState.value = AccountState(isLoading = true)
        viewModelScope.launch {
            userId?.let {
                when (val result = userUseCases.getUserUseCase(it)) {
                    is Result.Error -> handleGetUserError(result.error)
                    is Result.Success -> {
                        _accountState.value = _accountState.value!!.copy(
                            isLoading = false, user = result.data)
                    }
                }
            }
        }
    }

    private fun handleGetUserError(error: DataError.Local) {
        val message = when (error) {
            DataError.Local.NOT_FOUND -> "User not found."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _accountState.value = _accountState.value!!.copy(
            isLoading = false,
            dataError = message
        )
    }

    fun logOutUser() {
        _accountState.value = _accountState.value!!.copy(isLoading = true)

        sharedPreferencesUseCases.logOutUserUseCase()
        _accountState.value = _accountState.value!!.copy(
            isLoading = false,
            isUserLoggedOut = true
        )
    }

    fun deleteUser() {
        _accountState.value = _accountState.value!!.copy(isLoading = true)
        val userId = _accountState.value!!.user!!.userId

        viewModelScope.launch {
            when(val result = userUseCases.deleteUserUseCase(userId)) {
                is Result.Error -> handleDeleteUserError(result.error)
                is Result.Success -> {
                    sharedPreferencesUseCases.logOutUserUseCase()
                    _accountState.value = _accountState.value!!.copy(
                        isLoading = false,
                        isDeletionCompleted = true
                    )
                }
            }
        }
    }

    private fun handleDeleteUserError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.DELETION_FAILED -> TODO()
            DataError.Local.UNKNOWN -> TODO()
            else -> null
        }
        _accountState.value = _accountState.value!!.copy(
            actionError = message
        )
    }
}