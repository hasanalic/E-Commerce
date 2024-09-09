package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_checkout.domain.model.CardValidationError
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentCardViewModel @Inject constructor(
    private val cardUseCases: CardUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _paymentCardState = MutableLiveData(PaymentCardState())
    val paymentCardState: LiveData<PaymentCardState> = _paymentCardState

    fun checkIfUserHaveAnyCard() {
        _paymentCardState.value = PaymentCardState(isLoading = true)
        viewModelScope.launch {
            val userId = sharedPreferencesUseCases.getUserIdUseCase() ?: ANOMIM_USER_ID

            when(val result = cardUseCases.getCardsByUserIdUseCase(userId)) {
                is Result.Error -> handleCheckIfUserHaveAnyCard(result.error)
                is Result.Success -> {
                    val cardList = result.data
                    var doesUserHaveCards = false
                    if (cardList.isNotEmpty()) {
                        doesUserHaveCards = true
                    }

                    _paymentCardState.value = PaymentCardState(
                        doesUserHaveCards = doesUserHaveCards,
                        userId = userId
                    )
                }
            }
        }
    }

    private fun handleCheckIfUserHaveAnyCard(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "User card information could not be accessed."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _paymentCardState.value = PaymentCardState(actionError = message)
    }

    fun onClickConfirm(cardName: String, cardNumber: String, month: String, year: String, cvv: String) {
        _paymentCardState.value = _paymentCardState.value!!.copy(isLoading = true, validationError = null)
        val result = cardUseCases.cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        when(result) {
            is Result.Error -> handleCardCalidationError(result.error)
            is Result.Success -> _paymentCardState.value = _paymentCardState.value!!.copy(
                isLoading = false,
                canUserContinueToNextStep = true,
                validationError = null
            )
        }
    }

    fun onClickConfirmWithSaveCard(cardName: String, cardNumber: String, month: String, year: String, cvv: String) {
        _paymentCardState.value = _paymentCardState.value!!.copy(isLoading = true, validationError = null)
        viewModelScope.launch {
            val result = cardUseCases.cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
            when(result) {
                is Result.Error -> handleCardCalidationError(result.error)
                is Result.Success -> saveCard(cardName, cardNumber)
            }
        }
    }

    private suspend fun saveCard(cardName: String, cardNumber: String) {
        val userId = _paymentCardState.value!!.userId
        val cardEntity = CardEntity(cardName, cardNumber, userId)
        when(val result = cardUseCases.insertCardEntityUseCase(cardEntity)) {
            is Result.Error -> handleSaveCardError(result.error)
            is Result.Success -> {
                _paymentCardState.value = _paymentCardState.value!!.copy(
                    isLoading = false,
                    canUserContinueToNextStep = true,
                    cardId = result.data,
                    validationError = null
                )
            }
        }
    }

    private fun handleSaveCardError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.INSERTION_FAILED -> "Card could not be saved."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _paymentCardState.value = _paymentCardState.value!!.copy(
            isLoading = false,
            dataError = message
        )
    }

    private fun handleCardCalidationError(error: CardValidationError) {
        val message = when(error) {
            CardValidationError.EMPTY_CARD_NAME -> "Card name cannot be blank."
            CardValidationError.SHORT_CARD_NAME -> "The card name is too short."
            CardValidationError.EMPTY_CARD_NUMBER -> "Card number cannot be blank."
            CardValidationError.INVALID_CARD_NUMBER -> "The card number is invalid."
            CardValidationError.EMPTY_MONTH -> "Month value cannot be empty."
            CardValidationError.INVALID_MONTH -> "Invalid month value."
            CardValidationError.EMPTY_YEAR -> "The year value cannot be blank."
            CardValidationError.INVALID_YEAR -> "Invalid year value."
            CardValidationError.EMPTY_CVV -> "The CVV value cannot be empty."
            CardValidationError.INVALID_CVV -> "Invalid CVV value."
        }
        _paymentCardState.value = _paymentCardState.value!!.copy(
            isLoading = false,
            validationError = message
        )
    }
}