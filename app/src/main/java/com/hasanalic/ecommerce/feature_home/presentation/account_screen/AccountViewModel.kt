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

    fun getUser() {
        _accountState.value = AccountState(isLoading = true)
        viewModelScope.launch {
            val userId = sharedPreferencesUseCases.getUserIdUseCase()
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
            DataError.Local.NOT_FOUND -> "Kullanıcı bulunamadı."
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _accountState.value = _accountState.value!!.copy(
            isLoading = false,
            dataError = message
        )
    }

    fun logOutUser() {
        _accountState.value = _accountState.value!!.copy(isLoading = true)
        val userId = sharedPreferencesUseCases.getUserIdUseCase()

        if (userId != null) {
            sharedPreferencesUseCases.logOutUserUseCase()
            _accountState.value = _accountState.value!!.copy(
                isLoading = false,
                isUserLoggedOut = true
            )
        } else {
            _accountState.value =
                _accountState.value!!.copy(
                    isLoading = false,
                    actionError = "Bilinmeyen bir hata meydana geldi!"
                )
        }
    }
}