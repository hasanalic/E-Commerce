package com.hasanalic.ecommerce.feature_auth.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.feature_auth.domain.model.PasswordError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailError
import com.hasanalic.ecommerce.feature_auth.domain.model.InputError
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserEmailValidatorUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserInputValidatorUseCase
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.UserPasswordValidatorUseCase
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

    private fun handleInputValidationError(error: InputError) {
        val message = when(error) {
            InputError.EMPTY_NAME -> "İsim boş olamaz."
            InputError.EMPTY_EMAIL -> "Email boş olamaz."
            InputError.EMPTY_PASSWORD -> "Şifre boş olamaz."
        }
        _registerState.value = _registerState.value!!.copy(
            validationError = message,
            isLoading = false
        )
    }

    private fun handleEmailValidationError(error: EmailError) {
        val message = when(error) {
            EmailError.INVALID_FORMAT -> "Geçersiz email formatı."
        }
        _registerState.value = _registerState.value!!.copy(
            validationError = message,
            isLoading = false
        )
    }

    private fun handlePasswordValidationError(error: PasswordError) {
        when(error) {
            PasswordError.TOO_SHORT -> {
                _registerState.value = _registerState.value!!.copy(
                    validationError = "Şifre çok kısa",
                    isLoading = false
                )
            }
            PasswordError.NO_UPPERCASE -> {
                _registerState.value = _registerState.value!!.copy(
                    validationError = "Şifrede en az bir tane büyük harf karakter olmalı.",
                    isLoading = false
                )
            }
            PasswordError.NO_DIGIT -> {
                _registerState.value = _registerState.value!!.copy(
                    validationError = "Şifrede en az bir rakam olmalı.",
                    isLoading = false
                )
            }
        }
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