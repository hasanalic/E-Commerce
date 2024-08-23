package com.hasanalic.ecommerce.feature_checkout.presentation.payment_card_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import kotlinx.coroutines.launch
import javax.inject.Inject

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
}