package com.hasanalic.ecommerce.feature_checkout.presentation.cards_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasanalic.ecommerce.core.domain.model.DataError
import com.hasanalic.ecommerce.core.domain.model.Result
import com.hasanalic.ecommerce.core.domain.use_cases.shared_preferences.SharedPreferencesUseCases
import com.hasanalic.ecommerce.core.presentation.utils.UserConstants.ANOMIM_USER_ID
import com.hasanalic.ecommerce.feature_checkout.domain.use_cases.CardUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardUseCases: CardUseCases,
    private val sharedPreferencesUseCases: SharedPreferencesUseCases
) : ViewModel() {

    private var _cardsState = MutableLiveData(CardsState())
    val cardsState: LiveData<CardsState> = _cardsState

    fun getUserCards() {
        _cardsState.value = CardsState(isLoading = true)
        viewModelScope.launch {
            val userId = sharedPreferencesUseCases.getUserIdUseCase() ?: ANOMIM_USER_ID
            when(val result = cardUseCases.getCardsByUserIdUseCase(userId)) {
                is Result.Error -> handleGetUserCardsError(result.error)
                is Result.Success -> _cardsState.value = CardsState(cardList = result.data)
            }
        }
    }

    private fun handleGetUserCardsError(error: DataError.Local) {
        val message = when(error) {
            DataError.Local.NOT_FOUND -> "User cards not found."
            DataError.Local.UNKNOWN -> "An unknown error occurred."
            else -> null
        }
        _cardsState.value = CardsState(dataError = message)
    }
}