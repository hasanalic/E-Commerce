package com.hasanalic.ecommerce.feature_home.presentation.account_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _accountState = MutableLiveData(AccountState())
    val accountState: LiveData<AccountState> = _accountState

    fun getUser() {

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
            _accountState.value = _accountState.value!!.copy(actionError = "Bilinmeyen bir hata meydana geldi!")
        }
    }
}