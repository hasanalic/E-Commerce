package com.hasanalic.ecommerce.feature_auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_auth.domain.model.EmailValidationError
import com.hasanalic.ecommerce.feature_auth.domain.model.InputValidationError
import com.hasanalic.ecommerce.feature_auth.domain.model.PasswordValidationError
import com.hasanalic.ecommerce.feature_auth.domain.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {

    private var _loginState = MutableLiveData(LoginState())
    val loginState: LiveData<LoginState> = _loginState

    fun onLoginClick(email: String, password: String) {
        _loginState.value = LoginState(isLoading = true)

        val inputValidationResult = authUseCases.userInputValidatorUseCase(email = email, password = password)
        if (inputValidationResult is Result.Error) {
            handleInputValidationError(inputValidationResult.error)
            return
        }

        val emailValidationResult = authUseCases.userEmailValidatorUseCase(email)
        if (emailValidationResult is Result.Error) {
            handleEmailValidationError(emailValidationResult.error)
            return
        }

        when(val passwordValidationResult = authUseCases.userPasswordValidatorUseCase(password)) {
            is Result.Error -> handlePasswordValidationError(passwordValidationResult.error)
            is Result.Success -> loginUser(email, password)
        }
    }

    private fun handleEmailValidationError(error: EmailValidationError) {
        val errorMessage = when(error) {
            EmailValidationError.INVALID_FORMAT -> "Geçersiz email formatı."
        }
        _loginState.value = _loginState.value!!.copy(
            validationError = errorMessage,
            isLoading = false
        )
    }

    private fun handleInputValidationError(error: InputValidationError) {
        val errorMessage = when(error) {
            InputValidationError.EMPTY_NAME -> ""
            InputValidationError.EMPTY_EMAIL -> "Email boş olamaz."
            InputValidationError.EMPTY_PASSWORD -> "Şifre boş olamaz."
        }
        _loginState.value = _loginState.value!!.copy(
            validationError = errorMessage,
            isLoading = false
        )
    }

    private fun handlePasswordValidationError(error: PasswordValidationError) {
        val errorMessage = when(error) {
            PasswordValidationError.TOO_SHORT -> "Şifre çok kısa"
            PasswordValidationError.NO_UPPERCASE -> "Şifrede en az bir tane büyük harf karakter olmalı."
            PasswordValidationError.NO_DIGIT -> "Şifrede en az bir rakam olmalı."
        }
        _loginState.value = _loginState.value!!.copy(
            validationError = errorMessage,
            isLoading = false
        )
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            when(val result = authUseCases.getUserByEmailAndPassUseCase(email, password)) {
                is Result.Error -> handleLoginError(result.error)
                is Result.Success -> {
                    _loginState.value = LoginState(
                        isLoginSuccessful = true
                    )
                }
            }
        }
    }

    private fun handleLoginError(error: DataError.Local) {
        when(error) {
            DataError.Local.QUERY_FAILED -> TODO()
            DataError.Local.INSERTION_FAILED -> TODO()
            DataError.Local.UPDATE_FAILED -> TODO()
            DataError.Local.DELETION_FAILED -> TODO()
            DataError.Local.NOT_FOUND -> {
                _loginState.value = LoginState(
                    dataError = "Email veya şifre hatalı."
                )
            }
            DataError.Local.UNKNOWN -> TODO()
        }
    }

    /*
    fun updateUsersShoppingCartEntities(userId: String, anomimUserId: String) {
        _stateShoppingCartItems.value = Resource.Loading()
        viewModelScope.launch {
            val responseFromAnomimShoppingCartEntities = homeRepository.getShoppingCartItems(anomimUserId)

            if (responseFromAnomimShoppingCartEntities is Resource.Success) {
                val responseFromUsersShoppingCartEntities = homeRepository.getShoppingCartItems(userId)

                if (responseFromUsersShoppingCartEntities is Resource.Success) {

                    val anomimShoppingCartEntityList = responseFromAnomimShoppingCartEntities.data
                    val userShoppingCartEntityList = responseFromUsersShoppingCartEntities.data
                    var willBeAddedAnomimShoppingCartEntityList = anomimShoppingCartEntityList

                    if (userShoppingCartEntityList!!.isNotEmpty()) {
                        if (anomimShoppingCartEntityList!!.isNotEmpty()) {
                            for (userShoppingCartEntity in userShoppingCartEntityList) {
                                willBeAddedAnomimShoppingCartEntityList = willBeAddedAnomimShoppingCartEntityList!!.filter {willBeAddedShoppingCartEntity ->
                                    willBeAddedShoppingCartEntity.productId != userShoppingCartEntity.productId
                                }
                            }
                        }
                    }

                    if (anomimShoppingCartEntityList!!.isNotEmpty()) {
                        val productIdList = anomimShoppingCartEntityList.map { it.productId!! }
                        val responseDeleteShoppingCartItems = checkoutRepository.deleteShoppingCartItemsByProductIds(anomimUserId, productIdList)

                        if (responseDeleteShoppingCartItems is Resource.Success) {
                            if (willBeAddedAnomimShoppingCartEntityList!!.isNotEmpty()) {
                                willBeAddedAnomimShoppingCartEntityList.forEach {
                                    it.userId = userId
                                }
                                val responseInsertAllShoppingCartItems = homeRepository.insertAllShoppingCartItems(*willBeAddedAnomimShoppingCartEntityList.toTypedArray())

                                if (responseInsertAllShoppingCartItems is Resource.Success) {
                                    _stateShoppingCartItems.value = Resource.Success(true)
                                } else {
                                    _stateShoppingCartItems.value = Resource.Error(null,responseInsertAllShoppingCartItems.message?:"responseInsertAllShoppingCartItems")
                                }
                            } else {
                                _stateShoppingCartItems.value = Resource.Success(true)
                            }
                        } else {
                            _stateShoppingCartItems.value = Resource.Error(null,responseDeleteShoppingCartItems.message?:"responseDeleteShoppingCartItems")
                        }
                    } else {
                        _stateShoppingCartItems.value = Resource.Success(true)
                    }

                } else {
                    _stateShoppingCartItems.value = Resource.Error(null,responseFromUsersShoppingCartEntities.message?:"responseFromUsersShoppingCartEntities")
                }
            } else {
                _stateShoppingCartItems.value = Resource.Error(null,responseFromAnomimShoppingCartEntities.message?:"responseFromAnomimShoppingCartEntities")
            }
        }
    }

     */
}