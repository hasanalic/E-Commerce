package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardUseCases: CardUseCases
) : ViewModel() {

    private var _cardsState = MutableLiveData(CardsState())
    val cardsState: LiveData<CardsState> = _cardsState

    fun getUserCards(userId: String) {
        _cardsState.value = CardsState(isLoading = true)
        viewModelScope.launch {
            when(val result = cardUseCases.getCardsByUserIdUseCase(userId)) {
                is Result.Error -> handleGetUserCardsError(result.error)
                is Result.Success -> _cardsState.value = CardsState(cardList = result.data)
            }
        }
    }

    private fun handleGetUserCardsError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "Kullanıcı kartları bulunamadı."
            DataError.Local.UNKNOWN -> "Bilinmeyen bir hata meydana geldi."
            else -> null
        }
        _cardsState.value = CardsState(dataError = message)
    }
}