package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.data.local.entity.CardEntity
import com.hasanalic.ecommerce.feature_checkout.domain.model.CardValidationError
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentCardViewModel @Inject constructor(
    private val cardUseCases: CardUseCases
) : ViewModel() {

    private var _paymentCardState = MutableLiveData(PaymentCardState())
    val paymentCardState: LiveData<PaymentCardState> = _paymentCardState

    fun checkIfUserHaveAnyCard(userId: String) {
        _paymentCardState.value = PaymentCardState(isLoading = true)
        viewModelScope.launch {
            when(val result = cardUseCases.getCardsByUserIdUseCase(userId)) {
                is Result.Error -> handleCheckIfUserHaveAnyCard(result.error)
                is Result.Success -> {
                    val cardList = result.data
                    if (cardList.isNotEmpty()) {
                        _paymentCardState.value = PaymentCardState(doesUserHaveCards = true)
                    }
                }
            }
        }
    }

    private fun handleCheckIfUserHaveAnyCard(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Kullanıcı kart bilgilerine erişilemedi."
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _paymentCardState.value = PaymentCardState(actionError = message)
    }

    fun onClickConfirm(cardName: String, cardNumber: String, month: String, year: String, cvv: String) {
        _paymentCardState.value = _paymentCardState.value!!.copy(isLoading = true)
        val result = cardUseCases.cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        when(result) {
            is Result.Error -> handleCardCalidationError(result.error)
            is Result.Success -> _paymentCardState.value = _paymentCardState.value!!.copy(
                isLoading = false,
                canUserContinueToNextStep = true
            )
        }
    }

    fun onClickConfirmWithSaveCard(cardName: String, cardNumber: String, month: String, year: String, cvv: String, userId: String) {
        _paymentCardState.value = _paymentCardState.value!!.copy(isLoading = true)
        val result = cardUseCases.cardValidatorUseCase(cardName, cardNumber, month, year, cvv)
        when(result) {
            is Result.Error -> handleCardCalidationError(result.error)
            is Result.Success -> saveCard(cardName, cardNumber, userId)
        }
    }

    private fun saveCard(cardName: String, cardNumber: String, userId: String) {
        viewModelScope.launch {
            val cardEntity = CardEntity(cardName, cardNumber, userId)
            when(val result = cardUseCases.insertCardEntityUseCase(cardEntity)) {
                is Result.Error -> handleSaveCardError(result.error)
                is Result.Success -> {
                    _paymentCardState.value = _paymentCardState.value!!.copy(
                        isLoading = false,
                        canUserContinueToNextStep = true,
                        cardId = result.data
                    )
                }
            }
        }
    }

    private fun handleSaveCardError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.INSERTION_FAILED -> "Kart kaydedilemedi."
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _paymentCardState.value = _paymentCardState.value!!.copy(
            isLoading = false,
            dataError = message
        )
    }

    private fun handleCardCalidationError(error: CardValidationError) {
        val message = when(error) {
            CardValidationError.EMPTY_CARD_NAME -> "Kart ismi boş olamaz."
            CardValidationError.SHORT_CARD_NAME -> "Kart ismi çok kısa."
            CardValidationError.EMPTY_CARD_NUMBER -> "Kart numarası boş olmaz."
            CardValidationError.INVALID_CARD_NUMBER -> "Kart numarası geçersiz."
            CardValidationError.EMPTY_MONTH -> "Ay değeri boş olamaz."
            CardValidationError.INVALID_MONTH -> "Geçersiz ay değeri."
            CardValidationError.EMPTY_YEAR -> "Yıl değeri boş olamaz."
            CardValidationError.INVALID_YEAR -> "Geçersiz yıl değeri."
            CardValidationError.EMPTY_CVV -> "CVV değeri boş olamaz."
            CardValidationError.INVALID_CVV -> "Geçersiz CVV değeri."
        }
        _paymentCardState.value = _paymentCardState.value!!.copy(
            isLoading = false,
            validationError = message
        )
    }
}