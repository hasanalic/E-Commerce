package com.hasanalic.ecommerce.feature_auth.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.feature_auth.domain.model.PasswordValidationError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailValidationError
import com.hasanalic.ecommerce.feature_auth.domain.model.InputValidationError
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
): ViewModel() {

    private var _registerState = MutableLiveData(RegisterState())
    val registerState: LiveData<RegisterState> = _registerState

    fun onRegisterClick(name: String, email: String, password: String) {
        _registerState.value = RegisterState(
            isLoading = true
        )

        val inputValidationResult = authUseCases.userInputValidatorUseCase(name, email, password)
        if (inputValidationResult is Result.Error) {
            handleInputValidationError(inputValidationResult.error)
            return
        }

        val emailValidationResult = authUseCases.userEmailValidatorUseCase(email)
        if (emailValidationResult is Result.Error) {
            handleEmailValidationError(emailValidationResult.error)
            return
        }

        when(val result = authUseCases.userPasswordValidatorUseCase(password)) {
            is Result.Error -> {
                handlePasswordValidationError(result.error)
            }
            is Result.Success -> {
                registerUser(name, email, password)
            }
        }
    }

    private fun handleInputValidationError(error: InputValidationError) {
        val message = when(error) {
            InputValidationError.EMPTY_NAME -> "İsim boş olamaz."
            InputValidationError.EMPTY_EMAIL -> "Email boş olamaz."
            InputValidationError.EMPTY_PASSWORD -> "Şifre boş olamaz."
        }
        _registerState.value = _registerState.value!!.copy(
            validationError = message,
            isLoading = false
        )
    }

    private fun handleEmailValidationError(error: EmailValidationError) {
        val message = when(error) {
            EmailValidationError.INVALID_FORMAT -> "Geçersiz email formatı."
        }
        _registerState.value = _registerState.value!!.copy(
            validationError = message,
            isLoading = false
        )
    }

    private fun handlePasswordValidationError(error: PasswordValidationError) {
        val message = when(error) {
            PasswordValidationError.TOO_SHORT -> "Şifre çok kısa"
            PasswordValidationError.NO_UPPERCASE -> "Şifrede en az bir tane büyük harf karakter olmalı."
            PasswordValidationError.NO_DIGIT -> "Şifrede en az bir rakam olmalı."
        }

        _registerState.value = _registerState.value!!.copy(
            validationError = message,
            isLoading = false
        )
    }

    private fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            when(val result = authUseCases.insertUserUseCase(name, email, password)) {
                is Result.Error -> handleRegisterError(result.error)
                is Result.Success -> {
                    _registerState.value = RegisterState(
                        isRegistrationSuccessful = true
                    )
                }
            }
        }
    }

    private fun handleRegisterError(dataError: DataError.Local) {
        when(dataError) {
            DataError.Local.QUERY_FAILED -> {}
            DataError.Local.INSERTION_FAILD -> {
                _registerState.value = RegisterState(
                    dataError = "Kullanıcı kaydedilemedi.",
                    isLoading = false
                )
            }
            DataError.Local.UPDATE_FAILED -> {}
            DataError.Local.DELETION_FAILED -> {}
            DataError.Local.UNKNOWN -> {}
        }
    }
}