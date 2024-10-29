package com.hasanalic.ecommerce.feature_auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailValidationError
import com.hasanalic.ecommerce.feature_auth.domain.model.InputValidationError
import com.hasanalic.ecommerce.feature_auth.domain.model.PasswordValidationError
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sharedPreferencesUseCases: SharedPreferencesUseCases,
    private val authUseCases: AuthUseCases
): ViewModel() {

    var email = MutableLiveData("")
    var password = MutableLiveData("")

    private var _loginState = MutableLiveData(LoginState())
    val loginState: LiveData<LoginState> = _loginState

    fun onLoginClick() {
        val emailValue = email.value ?: ""
        val passwordValue = password.value ?: ""

        _loginState.value = _loginState.value?.copy(isLoading = true)

        val inputValidationResult = authUseCases.userInputValidatorUseCase(email = emailValue, password = passwordValue)
        if (inputValidationResult is Result.Error) {
            handleInputValidationError(inputValidationResult.error)
            return
        }

        val emailValidationResult = authUseCases.userEmailValidatorUseCase(emailValue)
        if (emailValidationResult is Result.Error) {
            handleEmailValidationError(emailValidationResult.error)
            return
        }

        when(val passwordValidationResult = authUseCases.userPasswordValidatorUseCase(passwordValue)) {
            is Result.Error -> handlePasswordValidationError(passwordValidationResult.error)
            is Result.Success -> loginUser(emailValue, passwordValue)
        }
    }

    private fun handleEmailValidationError(error: EmailValidationError) {
        val errorMessage = when(error) {
            EmailValidationError.INVALID_FORMAT -> "Invalid email format."
        }
        _loginState.value = _loginState.value!!.copy(
            validationError = errorMessage,
            isLoading = false
        )
    }

    private fun handleInputValidationError(error: InputValidationError) {
        val errorMessage = when(error) {
            InputValidationError.EMPTY_NAME -> ""
            InputValidationError.EMPTY_EMAIL -> "Email cannot be empty."
            InputValidationError.EMPTY_PASSWORD -> "Password cannot be empty."
        }
        _loginState.value = _loginState.value!!.copy(
            validationError = errorMessage,
            isLoading = false
        )
    }

    private fun handlePasswordValidationError(error: PasswordValidationError) {
        val errorMessage = when(error) {
            PasswordValidationError.TOO_SHORT -> "Password is too short."
            PasswordValidationError.NO_UPPERCASE -> "Password must contain at least one uppercase letter."
            PasswordValidationError.NO_DIGIT -> "Password must contain at least one digit."
        }
        _loginState.value = _loginState.value!!.copy(
            validationError = errorMessage,
            isLoading = false
        )
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            when(val result = authUseCases.loginUserWithEmailAndPasswordUseCase(email, password)) {
                is Result.Error -> handleLoginError(result.error)
                is Result.Success -> {
                    setIsLoginSuccessfulToTrueAndSaveUserId(result.data.toString())
                }
            }
        }
    }

    private fun setIsLoginSuccessfulToTrueAndSaveUserId(userId: String) {
        sharedPreferencesUseCases.saveUserIdUseCase(userId)
        _loginState.value = LoginState(
            isLoginSuccessful = true
        )
    }

    private fun handleLoginError(error: DataError.Local) {
        when(error) {
            DataError.Local.QUERY_FAILED -> TODO()
            DataError.Local.INSERTION_FAILED -> TODO()
            DataError.Local.UPDATE_FAILED -> TODO()
            DataError.Local.DELETION_FAILED -> TODO()
            DataError.Local.NOT_FOUND -> {
                _loginState.value = LoginState(
                    dataError = "Email or password is incorrect."
                )
            }
            DataError.Local.UNKNOWN -> TODO()
        }
    }

    fun clearValidationError() {
        _loginState.value = _loginState.value?.copy(validationError = null)
    }

    fun clearDataError() {
        _loginState.value = _loginState.value?.copy(dataError = null)
    }
}